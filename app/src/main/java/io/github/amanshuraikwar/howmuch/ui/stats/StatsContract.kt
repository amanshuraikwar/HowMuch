package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.ExpenseCategorySummary
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.SheetsHelper
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary.ExpenseCategorySummaryListItem
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface StatsContract {

    interface View : BaseView, UiMessageView, LoadingView, GoogleAccountView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class StatsPresenter @Inject constructor(appBus: AppBus,
                                             dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        @Inject
        lateinit var sheetsHelper: SheetsHelper

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchStats()
            }
        }

        private fun fetchStats() {

            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMap {
                        id ->
                        sheetsHelper.fetchTransactions(
                                spreadsheetId = id,
                                googleAccountCredential = getView()?.getGoogleAccountCredential(getAccount()!!)!!
                        )
                    }
                    .map {
                        transactions ->
                        val listItems =
                                mutableListOf(transactions.getTotalListItem())
                        listItems.addAll(transactions.getListItems())
                        return@map listItems
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
                    .groupBy { it.category }
                    .mapValues {
                        (_, transactions) ->
                        transactions.sumByDouble { it.amount }
                    }
                    .map {
                        (category, amountSum) ->
                        ExpenseCategorySummary(category, category, "0", "$amountSum")
                    }
                    .map { ExpenseCategorySummaryListItem(it) }


        }

        private fun List<Transaction>.getTotalListItem(): ListItem<*, *> {

            return ExpenseCategorySummaryListItem(
                    ExpenseCategorySummary(
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
    }
}