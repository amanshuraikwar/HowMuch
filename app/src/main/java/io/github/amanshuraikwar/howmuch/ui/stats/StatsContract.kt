package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.list.stats.Stats
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.stats.StatsListItem
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.HorizontalList
import io.github.amanshuraikwar.howmuch.ui.list.items.WalletItem
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface StatsContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
        fun startWalletActivity(wallet: Wallet)
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

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchStats()
            }
        }

        private fun fetchStats() {

            getDataManager()
                    .getAllTransactions()
                    .map { it.toList() }
                    .flatMap {
                        list ->
                        getDataManager()
                                .getAllWallets()
                                .map {
                                    wallets ->
                                    wallets.map {
                                        wallet ->
                                        WalletItem
                                                .Item(WalletItem(

                                                        wallet.copy(
                                                                balance =
                                                                list.filter {
                                                                    it.walletId == wallet.id
                                                                }.sumByDouble { if (it.type == TransactionType.DEBIT) -it.amount else it.amount }
                                                        )
                                                ))
                                                .setOnClickListener(
                                                        object : WalletItem.WalletOnClickListener {
                                                            override fun onClick(wallet: Wallet) {
                                                                getView()?.startWalletActivity(wallet)
                                                            }
                                                        }
                                                )
                                    }
                                }
                                .map {
                                    HorizontalList.Eager.Item(
                                            HorizontalList.Eager("wallets", it)
                                    )
                                }
                                .map {
                                    val newList =
                                            mutableListOf<ListItem<*, *>>(
                                                    HeaderListItem("Wallets"),
                                                    it,
                                                    HeaderListItem("Statistics")
                                            )
                                    newList.add(list.getTotalListItem())
                                    newList.addAll(list.getListItems())
                                    return@map newList
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

        private fun List<Transaction>.getListItems(): List<ListItem<*, *>> {

            return this
                    .groupBy { it.categoryId }
                    .mapValues {
                        (_, transactions) ->
                        transactions.sumByDouble { it.amount }
                    }
                    .map {
                        (category, amountSum) ->
                        Stats(category, category, "0", "%.2f".format(amountSum))
                    }
                    .map { StatsListItem(it) }


        }

        private fun List<Transaction>.getTotalListItem(): ListItem<*, *> {

            return StatsListItem(
                    Stats(
                            "Total",
                            "Total",
                            "0",
                            "${this.sumByDouble { it.amount }}"
                    )
            )
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