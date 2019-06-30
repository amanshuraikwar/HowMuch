package io.github.amanshuraikwar.howmuch.ui.monthlybudget

import android.util.Log
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.base.ui.base.LoadingView
import io.github.amanshuraikwar.howmuch.base.ui.base.UiMessageView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.pie.LimitLineView
import io.github.amanshuraikwar.howmuch.graph.pie.PieView
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
                            spentAmount,
                            limit,
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
                                LimitLineView.Item(
                                        it.key.split("-")[0].toFloat(),
                                        it.value.toFloat()
                                )
                            }
                            .sortedBy { it.x }

            val curDayOfMonth = Util.getDayOfMonth()

            return MonthlyBudgetGraph.Item(
                    MonthlyBudgetGraph(
                            data,
                            limit.toFloat(),
                            curDayOfMonth.toFloat()
                    )
            )
        }

        private fun Double.money(): Double = "%.2f".format(this).toDouble()

        @Suppress("MoveLambdaOutsideParentheses")
        private fun List<Transaction>.getCategoryItems(categories: List<Category>)
                : List<ListItem<*, *>> {

            val categoryAmountMap = {
                val categoryIdTxnListMap = this.groupBy { it.categoryId }
                categories
                        .filter { it.type == TransactionType.DEBIT }
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
                                            getView()?.startHistoryActivity("category_id=${it.id}")
                                        }
                                )
                        )
                    }
                    .forEachIndexed {
                        index, item ->
                        list.add(item)
                        if (index != categories.size - 1) {
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
                                            .getMonthlyExpenseLimit()
                                            .map {
                                                listItems.add(txnList.getMonthlyExpenseLimitItem(it))
                                                listItems.add(Divider.Item())
                                                listItems.add(txnList.getGraphItem(it))
                                                listItems
                                            }
                                }
                                .map {
                                    it.add(Divider.Item())
                                    it
                                }
                                .map {
                                    it.add(
                                            PaddedHeader.Item(
                                                    PaddedHeader("Categories")
                                            )
                                    )
                                    it
                                }
                                .flatMap {
                                    listItems ->
                                    getDataManager()
                                            .getAllCategories()
                                            .map { it.toList() }
                                            .map { txnList.getCategoryItems(it) }
                                            .map {
                                                listItems.addAll(it)
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
        }

        override fun onRefreshClicked() {
        }

        override fun onPreviousMonthClicked() {
        }

        override fun onNextMonthClicked() {
        }

    }
}