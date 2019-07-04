package io.github.amanshuraikwar.howmuch.ui.wallets

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.date.DateHeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.StatWallet
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface WalletsContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startHistoryActivity(filter: String? = null)
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchItems()
            }
        }

        private fun fetchItems() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                    }
                    .flatMap {
                        txnList ->
                        getDataManager()
                                .getAllWallets()
                                .map {
                                    walletList ->
                                    Pair(txnList, walletList.toList())
                                }
                    }
                    .flatMap {
                        pair ->
                        Observable
                                .fromCallable {
                                    mutableListOf<ListItem<*, *>>()
                                }
                                .map {
                                    prevList ->
                                    prevList.add(
                                            DateHeaderListItem("Wallets")
                                    )
                                    prevList
                                }
                                .map {
                                    prevList ->
                                    prevList.addAll(
                                            pair.first.getListItems(pair.second)
                                    )
                                    prevList
                                }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching items...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {

                                Log.d(tag, "fetchItems: Items: $it ")

                                getView()?.run {
                                    submitList(it)
                                    hideLoading()
                                }
                            },
                            {

                                it.printStackTrace()

                                Log.e(tag, "fetchItems: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()
        }

        fun Double.money(): Double = "%.2f".format(this).toDouble()

        @Suppress("MoveLambdaOutsideParentheses")
        private fun List<Transaction>.getListItems(wallets: List<Wallet>)
                : List<ListItem<*, *>> {

            val walletAmountMap = {
                        val categoryIdTxnListMap = this.groupBy { it.walletId }
                        wallets
                                .groupBy { it }
                                .mapValues {
                                    entry ->
                                    (categoryIdTxnListMap[entry.key.id]?.sumByDouble {
                                        if (it.type == TransactionType.DEBIT)
                                            -it.amount
                                        else
                                            it.amount
                                    } ?: 0.0).money()
                                }
                    }.invoke()

            return walletAmountMap.map {
                entry ->
                StatWallet.Item(
                        StatWallet(
                                entry.key,
                                entry.value,
                                {
                                    getView()?.startHistoryActivity("wallet_id=${it.id}")
                                }
                        )
                )
            }
        }

        override fun onRefreshClicked() {
            fetchItems()
        }

        override fun onRetryClicked() {
            fetchItems()
        }
    }
}