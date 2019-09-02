package io.github.amanshuraikwar.howmuch.ui.categories

import android.util.Log
import androidx.annotation.ColorInt
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.PieView
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.*
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CategoriesContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startHistoryActivity(
                category: Category,
                month: Int,
                year: Int
        )
        fun updateMonth(previousMonth: Boolean, monthName: String, nextMonth: Boolean)
        @ColorInt fun getCategoryColor(category: String): Int
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

        private fun fetchItems() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                    }
                    .map {
                        it.filter {
                            txn ->
                            val dateParts = txn.date.split("-")
                            dateParts[1].toInt() == displayedMonth
                                    && dateParts[2].toInt() == displayedYear
                        }
                    }
                    .flatMap {
                        txnList ->
                        getDataManager()
                                .getAllCategories()
                                .map {
                                    categoriesList ->
                                    Pair(txnList, categoriesList.toList())
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
                                    prevList.addAll(
                                            pair.first.getListItems(
                                                    TransactionType.DEBIT,
                                                    pair.second
                                            )
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

        private fun Double.money(): Double = "%.2f".format(this).toDouble()

        @Suppress("MoveLambdaOutsideParentheses")
        private fun List<Transaction>.getListItems(transactionType: TransactionType,
                                                   categories: List<Category>)
                : List<ListItem<*, *>> {

            val filteredCategories = categories.filter { it.type == transactionType }

            val categoryAmountMap = {
                        val categoryIdTxnListMap = this.groupBy { it.categoryId }
                        filteredCategories
                                .groupBy { it }
                                .mapValues {
                                    entry ->
                                    (categoryIdTxnListMap[entry.key.id]?.sumByDouble { it.amount }
                                            ?: 0.0).money()
                                }
                    }.invoke()

            val list = mutableListOf<ListItem<*, *>>()

            val pieItems = categoryAmountMap.map {
                entry ->
                PieView.PieItem(
                        entry.key.name,
                        entry.value.toFloat(),
                        getView()!!.getCategoryColor(entry.key.name)
                )
            }

            val total = pieItems.sumByDouble { it.value.toDouble() }.money()

            list.add(Pie.Item(Pie(pieItems, total)))

            list.add(Divider.Item())

            list.add(StatHeader.Item(StatHeader("CATEGORIES")))

            categoryAmountMap.keys.forEachIndexed {

                index, category ->

                list.add(
                        StatCategory.Item(
                                StatCategory(
                                        category,
                                        categoryAmountMap[category] ?: 0.0,
                                        {
                                            getView()?.startHistoryActivity(
                                                    it,
                                                    displayedMonth,
                                                    displayedYear
                                            )
                                        }
                                )
                        )
                )

                if (index != filteredCategories.size - 1) {
                    list.add(DividerFrontPadded.Item())
                }
            }

            return list
        }

        override fun onRefreshClicked() {
            fetchItems()
        }

        override fun onRetryClicked() {
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