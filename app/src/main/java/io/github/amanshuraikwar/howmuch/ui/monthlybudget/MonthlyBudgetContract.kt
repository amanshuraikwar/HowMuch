package io.github.amanshuraikwar.howmuch.ui.monthlybudget

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.base.ui.base.LoadingView
import io.github.amanshuraikwar.howmuch.base.ui.base.UiMessageView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.MonthlyBudgetGraph
import io.github.amanshuraikwar.howmuch.ui.list.items.MonthlyExpenseLimit
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MonthlyBudgetContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
        fun startHistoryActivity(filter: String? = null)
        fun updateMonth(previousMonth: Boolean, monthName: String, nextMonth: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
        fun onPreviousMonthClicked()
        fun onNextMonthClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private var curMonth = 0
        private var curYear = 0

        private var displayedMonth = 0
        private var displayedYear = 0

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                curMonth = Util.getCurMonthNumber()
                curYear = Util.getCurYearNumber()
                displayedMonth = curMonth
                displayedYear = curYear
                fetchItems()
            }
        }

        private fun List<Transaction>.filterByDate(dayOfMonth: String = "[0-9]+",
                                                   month: String = "[0-9]+",
                                                   year: String = "[0-9]{4}")
                : List<Transaction> {
            return this.filter {
                it.date.matches(Regex("($dayOfMonth)-($month)-($year)"))
            }
        }

        private fun List<Transaction>.getMonthlyExpenseLimitItem(limit: Double)
                : ListItem<*, *> {

            val spentAmount = this.sumByDouble { it.amount }

            return MonthlyExpenseLimit.Item(
                    MonthlyExpenseLimit(
                            spentAmount,
                            limit,
                            {}
                    )
            )
        }

        private fun fetchItems() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                                .filter { txn -> txn.type == TransactionType.DEBIT }
                                .filterByDate(
                                        month = displayedMonth.toString(),
                                        year = displayedYear.toString()
                                )
                    }
                    .flatMap {
                        txnList ->
                        Observable
                                .fromCallable {
                                    mutableListOf<ListItem<*, *>>()
                                }
                                .flatMap {
                                    listItems ->
                                    getDataManager()
                                            .getMonthlyExpenseLimit()
                                            .map {
                                                listItems.add(txnList.getMonthlyExpenseLimitItem(it))
                                                listItems
                                            }
                                }
                                .map {
                                    listItems ->
                                    listItems.add(MonthlyBudgetGraph.Item(MonthlyBudgetGraph()))
                                    listItems
                                }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching items...")
                            clearSyncError()
                            updateMonth(
                                    true,
                                    "${Util.getMonthName(displayedMonth)} $displayedYear",
                                    !(displayedMonth == curMonth && displayedYear == curYear)
                            )
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

        override fun onRetryClicked() {
        }

        override fun onRefreshClicked() {
        }

        override fun onPreviousMonthClicked() {
        }

        override fun onNextMonthClicked() {
        }

    }
}