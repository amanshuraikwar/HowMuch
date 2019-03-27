package io.github.amanshuraikwar.howmuch.ui.history

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.SheetsHelper
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.date.DateListItem
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem
import io.github.amanshuraikwar.howmuch.ui.list.expense.TransactionListItem
import io.github.amanshuraikwar.howmuch.ui.list.expense.TransactionOnClickListener
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HistoryContract {

    interface View : BaseView, UiMessageView, LoadingView, GoogleAccountView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startTransactionActivity(transaction: Transaction)
        fun setSyncError()
        fun clearSyncError()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onTransactionEdited()
        fun onRefreshClicked()
    }

    class HistoryPresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), HistoryContract.Presenter {

        @Inject
        lateinit var sheetsHelper: SheetsHelper

        private val transactionOnClickListener= object : TransactionOnClickListener {
            override fun onClick(transaction: Transaction) {
                getView()?.startTransactionActivity(transaction)
            }
        }

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                attachToAppBus()
                fetchTransactions()
            }
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onTransactionAdded
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fetchTransactions()
                    }
                    .addToCleanup()

        }

        private fun fetchTransactions() {

            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMap {
                        id ->
                        getDataManager()
                                .getSheetTitles(
                                        id,
                                        getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                )
                                .map {
                                    sheetTitles ->
                                    sheetTitles
                                            .filterNot { it == "Metadata" }
                                            .map { Pair(id, it) }
                                }.flatMap { Observable.fromIterable(it) }
                    }
                    .concatMapEager {
                        idSheetTitlesPair ->
                        sheetsHelper
                                .fetchTransactions(
                                        idSheetTitlesPair.first,
                                        idSheetTitlesPair.second,
                                        getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                )
                    }
                    .toList()
                    .map { it.flatten() }
                    .map { it.getListItems() }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching transactions...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideLoading()
                                    submitList(it)
                                }

                            },
                            {
                                it.printStackTrace()
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

            val inputSorted =
                    this
                            .sortedWith(
                                    Comparator {
                                        t1,t2 ->

                                        if (t1.date == t2.date && t1.time == t2.time) {
                                            return@Comparator 0
                                        }

                                        if(Util.compareDateTime(
                                                        "${t1.date} ${t1.time}",
                                                        "${t2.date} ${t2.time}"
                                                )) {
                                            return@Comparator -1
                                        }

                                        return@Comparator 1
                                    }
                            )
                            .reversed()

            val list = mutableListOf<ListItem<*, *>>()

            var date = ""
            var i = 0
            while (i < inputSorted.size) {
                if (date != inputSorted[i].date) {
                    date = inputSorted[i].date
                    list.add(DateListItem(Util.beautifyDate(date).toUpperCase()))
                }
                list.add(TransactionListItem(inputSorted[i]).setOnClickListener(transactionOnClickListener))
                i += 1
            }
            if(list.size == 0) {
                list.add(EmptyListItem("O_O"))
            }

            return list
        }

        override fun onRetryClicked() {
        }

        override fun onTransactionEdited() {
            fetchTransactions()
        }

        override fun onRefreshClicked() {
            fetchTransactions()
        }
    }
}