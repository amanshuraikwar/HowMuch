package io.github.amanshuraikwar.howmuch.ui.category

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.BarView
import io.github.amanshuraikwar.howmuch.graph.BarView2
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.money
import io.github.amanshuraikwar.howmuch.ui.list.date.DateHeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.*
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.howmuch.ui.month.MonthPresenter
import io.github.amanshuraikwar.howmuch.ui.month.MonthPresenterImpl
import io.github.amanshuraikwar.howmuch.ui.month.MonthView
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CategoryContract {

    interface View : MonthView {
        fun getCategory(): Category?
        fun initUi(title: String, color1: Int, color2: Int)
        fun showEditDialog(category: Category)
        fun hideEditDialog()
        fun showEditDialogError(msg: String)
        fun showEditDialogLoading(msg: String)
        fun hideEditDialogLoading()
        fun startTransactionActivity(transaction: Transaction, category: Category)
    }

    interface Presenter : MonthPresenter<View> {
        fun onEditClicked()
        fun onEditSaveClicked(text: String)
        fun onTransactionEdited()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : MonthPresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var category: Category

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            category = getView()?.getCategory()!!
            getView()?.initUi(
                    category.name,
                    ViewUtil.getCategoryColor(category.name),
                    ViewUtil.getCategoryColor2(category.name)
            )
        }

        override fun getListItems(txnList: List<Transaction>): Observable<List<ListItem<*, *>>> {

            return Observable
                    .fromCallable {

                        val filteredTxnList = txnList.filter { it.categoryId == category.id}
                        val listItems = mutableListOf<ListItem<*, *>>()

                        listItems.add(
                                IconTitle.Item(
                                        IconTitle(
                                                category.name,
                                                ViewUtil.getCategoryIcon(category.name),
                                                ViewUtil.getCategoryColor(category.name),
                                                ViewUtil.getCategoryColor2(category.name)
                                        )
                                )
                        )

                        listItems.add(Divider.Item())

                        listItems.add(filteredTxnList.getMonthlyExpenseLimitItem(category))

                        listItems.add(Divider.Item())

                        listItems.add(filteredTxnList.getGraphItem())

                        listItems.add(Divider.Item())

                        listItems.addAll(filteredTxnList.getTxnListItems())

                        listItems
                    }
        }

        private fun List<Transaction>.getMonthlyExpenseLimitItem(category: Category)
                : ListItem<*, *> {

            val spentAmount = this.sumByDouble { it.amount }

            return Limit.Item(
                    Limit(
                            spentAmount.money(),
                            category.monthlyLimit.money(),
                            ViewUtil.getCategoryColor(category.name),
                            ViewUtil.getCategoryColor2(category.name)
                    )
            )
        }

        private fun List<Transaction>.getGraphItem(): ListItem<*, *> {

            val txnsByDate = this.groupBy { it.date.split("-")[0].toInt() }
            val barItems = mutableListOf<BarView.BarItem>()
            val daysInMonth = Util.daysInMonth(displayedMonth - 1, displayedYear)

            val curDay = Util.getDayOfMonth()

            for (i in 1..daysInMonth) {
                barItems.add(
                        BarView.BarItem(
                                if (i == curDay) {
                                    if (curMonth == displayedMonth
                                        && curYear == displayedYear) {
                                        "TODAY"
                                    } else {
                                        "THIS DAY"
                                    }
                                } else {
                                    ""
                                },
                                txnsByDate[i]?.sumByDouble { it.amount }?.toFloat() ?: 0f
                        )
                )
            }

            return MonthBarGraph.Item(
                    MonthBarGraph(
                            barItems,
                            ViewUtil.getCategoryColor(category.name),
                            ViewUtil.getCategoryColor2(category.name),
                            if (curMonth == displayedMonth
                                    && curYear == displayedYear) {
                                "TODAY"
                            } else {
                                "THIS DAY"
                            }
                    )
            )
        }

        private fun List<Transaction>.getTxnListItems(): List<ListItem<*, *>> {

            if(this.isEmpty()) {
                return mutableListOf()
            }

            val list = mutableListOf<ListItem<*, *>>()

            list.add(StatHeader.Item(StatHeader("TRANSACTIONS")))

            val inputSorted =
                    this
                            .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                            .reversed()

            var date = ""
            var i = 0

            val color1 = ViewUtil.getCategoryColor(category.name)
            val color2 = ViewUtil.getCategoryColor3(category.name)

            while (i < inputSorted.size) {

                if (date != inputSorted[i].date) {

                    list.add(
                            DateHeaderListItem(
                                    Util.beautifyDate(inputSorted[i].date).toUpperCase(),
                                    color1,
                                    color2,
                                    date == ""
                            )
                    )

                    date = inputSorted[i].date
                }

                list.add(
                        TransactionListItem(
                                transaction = inputSorted[i],
                                color1 = color1,
                                color2 = color2,
                                last = i == inputSorted.size - 1
                        ).setOnClickListener(
                                transactionOnClickListener
                        )
                )

                i += 1

            }

            return list
        }

        private val transactionOnClickListener =
                object : TransactionOnClickListener {
                    override fun onClick(transaction: Transaction) {
                        getView()?.startTransactionActivity(
                                transaction, category
                        )
                    }
                }

        override fun onEditClicked() {
            getView()?.showEditDialog(category)
        }

        override fun onEditSaveClicked(text: String) {

            if (text.isEmpty()) {
                getView()?.showEditDialogError("Monthly limit cannot be empty!")
                return
            }

            val newCategory = category.copy(monthlyLimit = text.toDouble())

            getDataManager()
                    .updateCategory(
                            newCategory
                    )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showEditDialogLoading("Saving...")
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideEditDialogLoading()
                                    hideEditDialog()
                                }
                                category = newCategory
                                fetchItems()
                            },
                            {
                                Log.e(tag, "onEditSaveClicked: updateCategory", it)
                                // todo handle specific errors
                                getView()?.run {
                                    hideEditDialogLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onTransactionEdited() {
            fetchItems()
        }
    }
}