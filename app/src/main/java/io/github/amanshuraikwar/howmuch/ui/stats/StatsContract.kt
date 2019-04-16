package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.list.stats.Stats
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.stats.StatsListItem
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.Button
import io.github.amanshuraikwar.howmuch.ui.list.items.HorizontalList
import io.github.amanshuraikwar.howmuch.ui.list.items.StatTotal
import io.github.amanshuraikwar.howmuch.ui.list.items.WalletItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
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
        fun startWalletActivity(wallet: Wallet)
        fun startTransactionActivity(transaction: Transaction)
        fun startHistoryActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
        fun onWalletEdited()
    }

    class StatsPresenter @Inject constructor(appBus: AppBus,
                                             dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private val transactionOnClickListener=
                object : TransactionOnClickListener {
                    override fun onClick(transaction: Transaction) {
                        getView()?.startTransactionActivity(transaction)
                    }
                }

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchStats()
            }
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
                                    prevList ->
                                    prevList.add(txnList.getTotalItem())
                                    prevList
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

        private fun List<Transaction>.getTotalItem(): ListItem<*, *> {

            val totalMap = this
                    .groupBy { it.type }
                    .mapValues {
                        txns ->
                        txns.value.sumByDouble { it.amount }
                    }

            val recentTrendMap = this
                    .groupBy { it.type }
                    .mapValues {

                        txns ->

                        if (txns.value.isNotEmpty()) {

                            txns.value
                                    .subList(
                                            maxOf(0, (txns.value.size - 1) - 6),
                                            txns.value.size
                                    )
                                    .sumByDouble { it.amount }

                        } else {
                            0.0
                        }
                    }
                    .mapValues {

                        txns ->

                        val total = totalMap[txns.key] ?: 0.0

                        if (total != 0.0) {
                            (txns.value / total) * 100
                        } else {
                            0.0
                        }
                    }

            return StatTotal.Item(
                    StatTotal(
                            totalMap[TransactionType.CREDIT]?.money() ?: 0.0,
                            recentTrendMap[TransactionType.CREDIT]?.toInt() ?: 0,
                            totalMap[TransactionType.DEBIT]?.money() ?: 0.0,
                            recentTrendMap[TransactionType.DEBIT]?.toInt() ?: 0
                    )
            )
        }

        private fun List<Transaction>.getListItems()
                : List<ListItem<*, *>> {

            if (this.isEmpty()) {
                return emptyList()
            }

            val list = mutableListOf<ListItem<*, *>>(
                    HeaderListItem("Recent Transactions")
            )

            list.addAll(
                    this
                            .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                            .subList(kotlin.math.max(0, (this.size - 1) - 3), this.size)
                            .reversed()
                            .map {
                                TransactionListItem(it, true).setOnClickListener(
                                        transactionOnClickListener
                                )
                            }
            )

            list.add(
                    Button
                            .Item(Button("See All"))
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

        override fun onWalletEdited() {
            fetchStats()
        }
    }
}