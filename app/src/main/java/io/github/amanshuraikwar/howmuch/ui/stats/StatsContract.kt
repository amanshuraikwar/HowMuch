package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.pie.BarView
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.*
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface StatsContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
        fun startTransactionActivity(transaction: Transaction)
        fun startHistoryActivity(filter: String? = null)
        fun startSettingsActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
        fun onTransactionEdited()
    }

    class StatsPresenter @Inject constructor(appBus: AppBus,
                                             dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private val transactionOnClickListener =
                object : TransactionOnClickListener {
                    override fun onClick(transaction: Transaction) {
                        getView()?.startTransactionActivity(transaction)
                    }
                }

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                attachToAppBus()
                fetchStats()
            }
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onTransactionAdded
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fetchStats()
                    }
                    .addToCleanup()

        }

        private fun fetchStats() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                    }
                    .flatMap {
                        txnList ->
                        Observable
                                .fromCallable {
                                    mutableListOf<ListItem<*, *>>()
                                }
                                .map {
                                    it.add(
                                            Bars.Item(
                                                    Bars(
                                                            listOf(
                                                                    BarView.BarItem("MON", 100f),
                                                                    BarView.BarItem("TUE", 200f),
                                                                    BarView.BarItem("WED", 50f),
                                                                    BarView.BarItem("THU", 20f),
                                                                    BarView.BarItem("FRI", 10f),
                                                                    BarView.BarItem("SAT", 0f),
                                                                    BarView.BarItem("SUN", 150f)
                                                            )
                                                    )
                                            )
                                    )
                                    it
                                }
                                .flatMap {
                                    prevList ->
                                    getDataManager()
                                            .getMonthlyExpenseLimit()
                                            .map {
                                                prevList.addAll(txnList.getTotalItems(it))
                                                prevList
                                            }
                                }
                                .map {
                                    prevList ->
                                    prevList.addAll(txnList.getListItems())
                                    prevList
                                }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching stats...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {

                                Log.d(tag, "fetchStats: Stats: $it ")

                                getView()?.run {
                                    submitList(it)
                                    hideLoading()
                                }
                            },
                            {

                                it.printStackTrace()

                                Log.e(tag, "fetchStats: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()
        }

        private fun Double.money(): Double = "%.2f".format(this).toDouble()

        private fun List<Transaction>.getTotalItems(limit: Double): List<ListItem<*, *>> {

            val firstDayOfMonth = Util.getFirstDayOfMonth()
            val lastSeventhDay = Util.getLastSeventhDay()

            val filteredTransactions = this.filter {
                Util.toTimeMillisec(it.date, it.time) >= firstDayOfMonth
            }

            val thisMonthTotalMap = filteredTransactions
                    .groupBy { it.type }
                    .mapValues {
                        txns ->
                        txns.value.sumByDouble {
                            it.amount
                        }
                    }

            val list = mutableListOf<ListItem<*, *>>()

            list.add(
                    MonthlyExpenseLimit.Item(
                            MonthlyExpenseLimit(
                                    thisMonthTotalMap[TransactionType.DEBIT] ?: 0.0,
                                    limit,
                                    { getView()?.startSettingsActivity() }
                            )
                    )
            )

            val creditAmount = thisMonthTotalMap[TransactionType.CREDIT] ?: 0.0
            val debitAmount = thisMonthTotalMap[TransactionType.DEBIT] ?: 0.0

            val creditTrend: Int
            val debitTrend: Int

            if (lastSeventhDay >= firstDayOfMonth) {

                val lastWeekTotalMap = filteredTransactions
                        .groupBy { it.type }
                        .mapValues {
                            txns ->
                            txns.value.sumByDouble {
                                if (Util.toTimeMillisec(it.date, it.time) > lastSeventhDay) {
                                    it.amount
                                } else {
                                    0.0
                                }
                            }
                        }

                val lastWeekTotalCredit = lastWeekTotalMap[TransactionType.CREDIT] ?: 0.0
                val lastWeekTotalDebit = lastWeekTotalMap[TransactionType.DEBIT] ?: 0.0

                val curTime = Util.getCurTimeMillisec()

                // alpha = t1 / t2
                // where t1 = curTime - lastSeventhDay
                // and t2 = curTime - monthStartDay

                // beta = x(t1) / x(t2)
                // where x(t1) is transaction amount in time t1
                // and x(t2) is transaction amount in time t2

                // for ideal (avg) case alpha = beta

                // if alpha != beta
                // then the transactions are up/down by (beta - alpha) * 100 percentage

                // 'curTime - firstDayOfMonth' will never be zero
                val alpha = (curTime - lastSeventhDay).toDouble() / (curTime - firstDayOfMonth).toDouble()

                val creditBeta = {
                    if (creditAmount != 0.0) {
                        lastWeekTotalCredit / creditAmount
                    } else {
                        if (lastWeekTotalCredit != 0.0) {
                            1.0
                        } else {
                            0.0
                        }
                    }
                }.invoke()

                val debitBeta = {
                    if (debitAmount != 0.0) {
                        lastWeekTotalDebit / debitAmount
                    } else {
                        if (lastWeekTotalDebit != 0.0) {
                            1.0
                        } else {
                            0.0
                        }
                    }
                }.invoke()



                creditTrend = if (creditAmount == 0.0) 0 else ((creditBeta - alpha) * 100).toInt()
                debitTrend = if (debitAmount == 0.0) 0 else ((debitBeta - alpha) * 100).toInt()

            } else {
                creditTrend = if (creditAmount != 0.0) 100 else 0
                debitTrend = if (debitAmount != 0.0) 100 else 0
            }


            list.add(
                    StatTotal.Item(
                            StatTotal(
                                    creditAmount.money(),
                                    creditTrend,
                                    debitAmount.money(),
                                    debitTrend,
                                    {
                                        getView()?.startHistoryActivity("transaction_type=CREDIT")
                                    },
                                    {
                                        getView()?.startHistoryActivity("transaction_type=DEBIT")
                                    }
                            )
                    )
            )

            return list
        }

        private fun List<Transaction>.getListItems()
                : List<ListItem<*, *>> {

            if (this.isEmpty()) {
                return emptyList()
            }

            val list = mutableListOf<ListItem<*, *>>(
                    StatHeader.Item(StatHeader("Recent Transactions"))
            )

            list.addAll(
                    this
                            .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                            .subList(kotlin.math.max(0, (this.size - 1) - 3), this.size)
                            .reversed()
                            .map {
                                StatTransaction.Item(
                                        StatTransaction(it, true)
                                ).setOnClickListener(
                                        transactionOnClickListener
                                )
                            }
            )

            list.add(
                    StatButton
                            .Item(StatButton("See All"))
                            .setOnClickListener {
                                getView()?.startHistoryActivity()
                            }
            )

            return list
        }

        override fun onRefreshClicked() {
            fetchStats()
        }

        override fun onRetryClicked() {
            fetchStats()
        }

        override fun onTransactionEdited() {
            fetchStats()
        }
    }
}