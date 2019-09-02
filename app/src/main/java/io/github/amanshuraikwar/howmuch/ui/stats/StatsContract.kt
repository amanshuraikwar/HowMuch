package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.BarView2
import io.github.amanshuraikwar.howmuch.protocol.Category
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
        fun startTransactionActivity(transaction: Transaction, category: Category)
        fun startHistoryActivity(filter: String? = null)
        fun startMonthlyBudgetActivity()
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
        private lateinit var categoriesMap: Map<String, Category>

        private val transactionOnClickListener =
                object : TransactionOnClickListener {
                    override fun onClick(transaction: Transaction) {
                        getView()?.startTransactionActivity(
                                transaction, categoriesMap[transaction.categoryId]!!
                        )
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

        private fun buildListItems(txnList: List<Transaction>)
                : Observable<List<ListItem<*, *>>> {

            return Observable
                    .fromCallable {
                        mutableListOf<ListItem<*, *>>()
                    }
                    .map {
                        prevList ->
                        prevList.add(txnList.getPastSevenDayItem())
                        //prevList.add(Divider.Item())
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(txnList.getBarViewListItem())
                        //prevList.add(Divider.Item())
                        prevList
                    }
                    .flatMap {
                        prevList ->
                        getDataManager()
                                .getAllCategories()
                                .map { it.filter { it.type == TransactionType.DEBIT } }
                                .map {
                                    val total = it.sumByDouble { it.monthlyLimit }
                                    prevList.addAll(txnList.getThisMonthDayItems(total))
                                    prevList.addAll(
                                            txnList.getListItems(
                                                    it.groupBy { it.id }.mapValues { it.value[0] }
                                            )
                                    )
                                    prevList
                                }
                    }
        }

        private fun fetchStats() {

            getDataManager()
                    .getAllCategories()
                    .map {
                        categoriesMap =
                                it
                                        .filter { it.type == TransactionType.DEBIT }
                                        .groupBy { it.id }
                                        .mapValues { it.value[0] }
                    }
                    .flatMap {
                        getDataManager().getAllTransactions()
                    }
                    .map {
                        it.toList()
                    }
                    .flatMap {
                        buildListItems(it)
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

        private fun List<Transaction>.getBarViewListItem(): ListItem<*, *> {

            var list = this

            val lastSeventhDay = Util.getLastSeventhDay()

            list = list.filter {
                Util.toTimeMillisec(it.date, it.time) > lastSeventhDay
            }

            val txnsByType = list.groupBy { it.type }

            val debitDayAmountMap = linkedMapOf<String, Double>()

            var i = 1
            while (i > -6) {
                debitDayAmountMap[Util.getDateStrMoved(--i)] = 0.0
            }

            txnsByType[TransactionType.DEBIT]?.forEach {
                if (debitDayAmountMap[it.date] != null) {
                    debitDayAmountMap[it.date] = debitDayAmountMap[it.date]!!.plus(it.amount)
                }
            }

            return Bars.Item(
                    Bars(
                          debitDayAmountMap.map {
                              entry ->
                              BarView2.BarItem(
                                      Util.getWeekDay(entry.key).toUpperCase(),
                                      entry.value.toFloat()
                              )
                          }.reversed()
                    )
            )
        }

        private fun Double.money(): Double = "%.2f".format(this).toDouble()

        private fun List<Transaction>.getPastSevenDayItem(): ListItem<*, *> {

            val lastSeventhDay = Util.getLastSeventhDay()

            val filteredTransactions = this.filter {
                Util.toTimeMillisec(it.date, it.time) >= lastSeventhDay
            }

            val thisMonthTotalMap = filteredTransactions
                    .groupBy { it.type }
                    .mapValues {
                        txns ->
                        txns.value.sumByDouble {
                            it.amount
                        }
                    }

            val debitAmount = thisMonthTotalMap[TransactionType.DEBIT] ?: 0.0

            val trend = getTrend(debitAmount, filteredTransactions)

            return StatTotal.Item(
                            StatTotal(
                                    "Last 7 days",
                                    thisMonthTotalMap[TransactionType.DEBIT]?.money() ?: 0.0,
                                    trend
                            )
                    )
        }

        private fun getTrend(amount: Double,
                             transactions: List<Transaction>): Int {

            val trend: Int

            val firstDayOfMonth = Util.getFirstDayOfMonth()
            val lastSeventhDay = Util.getLastSeventhDay()

            if (lastSeventhDay >= firstDayOfMonth) {

                val lastWeekTotalMap = transactions
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

                val debitBeta = {
                    if (amount != 0.0) {
                        lastWeekTotalDebit / amount
                    } else {
                        if (lastWeekTotalDebit != 0.0) {
                            1.0
                        } else {
                            0.0
                        }
                    }
                }.invoke()

                trend = if (amount == 0.0) 0 else ((debitBeta - alpha) * 100).toInt()

            } else {
                trend = if (amount != 0.0) 100 else 0
            }

            return trend
        }

        private fun List<Transaction>.filterByDate(month: Int,
                                                   year: Int)
                : List<Transaction> {
            return this.filter {
                val parts = it.date.split("-")
                parts[1].toInt() == month && parts[2].toInt() == year
            }
        }

        private fun List<Transaction>.getThisMonthDayItems(limit: Double): List<ListItem<*, *>> {

            val curMonth = Util.getCurMonthNumber()
            val curYear = Util.getCurYearNumber()

            val filteredTransactions = this.filterByDate(curMonth, curYear)

            val thisMonthTotalMap = filteredTransactions
                    .groupBy { it.type }
                    .mapValues {
                        txns ->
                        txns.value.sumByDouble {
                            it.amount
                        }
                    }

            val debitAmount = thisMonthTotalMap[TransactionType.DEBIT] ?: 0.0

            val list = mutableListOf<ListItem<*, *>>()

            list.add(
                    MonthlyExpenseLimit.Item(
                            MonthlyExpenseLimit(
                                    debitAmount.money(),
                                    limit.money(),
                                    { getView()?.startMonthlyBudgetActivity() }
                            )
                    )
            )

            return list
        }

        private fun List<Transaction>.getListItems(categoriesMap: Map<String, Category>)
                : List<ListItem<*, *>> {

            if (this.isEmpty()) {
                return emptyList()
            }

            val list = mutableListOf<ListItem<*, *>>(
                    //Divider.Item(),
                    StatHeader.Item(StatHeader("Recent Transactions"))
            )

            this
                    .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                    .subList(kotlin.math.max(0, (this.size - 1) - 3), this.size)
                    .reversed()
                    .forEachIndexed {
                        index, txn ->

                        list.add(
                                StatTransaction.Item(
                                        StatTransaction(txn, categoriesMap[txn.categoryId]!!)
                                ).setOnClickListener(
                                        transactionOnClickListener
                                )
                        )

                        if (index != this@getListItems.size - 1) {
                            list.add(DividerFrontPadded.Item())
                        }
                    }

            list.add(
                    StatButton
                            .Item(StatButton("See More"))
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