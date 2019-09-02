package io.github.amanshuraikwar.howmuch.ui.monthlybudget

import android.util.Log
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.base.ui.base.LoadingView
import io.github.amanshuraikwar.howmuch.base.ui.base.UiMessageView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.BudgetLineView
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.*
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MonthlyBudgetContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startHistoryActivity(
                category: Category,
                month: Int,
                year: Int
        )
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

        private fun List<Transaction>.filterByDate(month: Int,
                                                   year: Int)
                : List<Transaction> {
            return this.filter {
                val parts = it.date.split("-")
                parts[1].toInt() == month && parts[2].toInt() == year
            }
        }

        private fun List<Transaction>.getMonthlyExpenseLimitItem(limit: Double)
                : ListItem<*, *> {

            val spentAmount = this.sumByDouble { it.amount }

            return Limit.Item(
                    Limit(
                            spentAmount.money(),
                            limit.money(),
                            R.color.monthlyBudgetLimitPrimary,
                            R.color.monthlyBudgetLimitSecondary
                    )
            )
        }

        private fun List<Transaction>.getGraphItem(limit: Double)
                : ListItem<*, *> {

            val data =
                    this
                            .groupBy { it.date }
                            .mapValues { it.value.sumByDouble { it.amount } }
                            .map {
                                BudgetLineView.Item(
                                        it.key.split("-")[0].toInt(),
                                        it.value.toFloat()
                                )
                            }
                            .sortedBy { it.x }

            val curDayOfMonth = Util.getDayOfMonth()

            return MonthlyBudgetGraph.Item(
                    MonthlyBudgetGraph(
                            data,
                            limit.toFloat(),
                            curDayOfMonth.toFloat(),
                            displayedMonth == curMonth
                    )
            )
        }

        private fun Double.money(): Double = "%.2f".format(this).toDouble()

        @Suppress("MoveLambdaOutsideParentheses")
        private fun List<Transaction>.getCategoryItems(categories: List<Category>)
                : List<ListItem<*, *>> {

            val debitCategories = categories.filter { it.type == TransactionType.DEBIT }

            val categoryAmountMap = {
                val categoryIdTxnListMap = this.groupBy { it.categoryId }
                debitCategories
                        .groupBy { it }
                        .mapValues {
                            entry ->
                            (categoryIdTxnListMap[entry.key.id]?.sumByDouble { it.amount }
                                    ?: 0.0).money()
                        }
            }.invoke()

            val list = mutableListOf<ListItem<*, *>>()

            categoryAmountMap
                    .map {
                        entry ->
                        StatCategoryPadded.Item(
                                StatCategoryPadded(
                                        entry.key,
                                        entry.value,
                                        {
                                            getView()?.startHistoryActivity(
                                                    it,
                                                    displayedMonth,
                                                    displayedYear
                                            )
                                        }
                                )
                        )
                    }
                    .forEachIndexed {
                        index, item ->
                        list.add(item)
                        if (index != debitCategories.size - 1) {
                            list.add(DividerFrontPadded.Item())
                        }
                    }

            return list
        }

        private fun fetchItems() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                                .filter { txn -> txn.type == TransactionType.DEBIT }
                                .filterByDate(
                                        month = displayedMonth,
                                        year = displayedYear
                                )
                    }
                    .flatMap {
                        txnList ->
                        Observable
                                .fromCallable {
                                    mutableListOf<ListItem<*, *>>()
                                }
                                .map {
                                    it.add(
                                            IconTitle.Item(
                                                    IconTitle(
                                                            "Monthly Budget",
                                                            R.drawable.round_date_range_24,
                                                            R.color.monthlyBudgetLimitPrimary,
                                                            R.color.monthlyBudgetLimitSecondary
                                                    )
                                            )
                                    )
                                    it
                                }
                                .map {
                                    it.add(Divider.Item())
                                    it
                                }
                                .flatMap {
                                    listItems ->
                                    getDataManager()
                                            .getAllCategories()
                                            .map { it.filter { it.type == TransactionType.DEBIT } }
                                            .map {
                                                val total = it.sumByDouble { it.monthlyLimit }
                                                listItems.add(txnList.getMonthlyExpenseLimitItem(total))
                                                listItems.add(Divider.Item())
                                                listItems.add(txnList.getGraphItem(total))
                                                listItems.add(Divider.Item())
                                                listItems.add(
                                                        StatHeader.Item(
                                                                StatHeader("Categories")
                                                        )
                                                )
                                                listItems.addAll(txnList.getCategoryItems(it))
                                                listItems
                                            }
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
            fetchItems()
        }

        override fun onRefreshClicked() {
            fetchItems()
        }

        override fun onPreviousMonthClicked() {
            if (displayedMonth == 1) {
                displayedMonth = 12
                displayedYear--
            } else {
                displayedMonth--
            }
            fetchItems()
        }

        override fun onNextMonthClicked() {
            if (displayedMonth == 12) {
                displayedMonth = 1
                displayedYear++
            } else {
                displayedMonth++
            }
            fetchItems()
        }

    }
}