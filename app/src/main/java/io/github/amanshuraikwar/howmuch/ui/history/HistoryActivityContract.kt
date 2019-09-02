package io.github.amanshuraikwar.howmuch.ui.history

import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.graph.BarView
import io.github.amanshuraikwar.howmuch.graph.BarView2
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.list.date.DateHeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.*
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.howmuch.ui.month.MonthPresenter
import io.github.amanshuraikwar.howmuch.ui.month.MonthPresenterImpl
import io.github.amanshuraikwar.howmuch.ui.month.MonthView
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import javax.inject.Inject

interface HistoryActivityContract {

    interface View : MonthView {
        fun startTransactionActivity(transaction: Transaction, category: Category)
    }

    interface Presenter : MonthPresenter<View> {
        fun onTransactionEdited()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : MonthPresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var categoriesMap: Map<String, Category>

        override fun getListItems(txnList: List<Transaction>): Observable<List<ListItem<*, *>>> {

            return Observable
                    .fromCallable {

                        val listItems = mutableListOf<ListItem<*, *>>()

                        listItems.add(
                                IconTitle.Item(
                                        IconTitle(
                                                "History",
                                                R.drawable.round_history_24,
                                                R.color.green,
                                                R.color.lightGreen
                                        )
                                )
                        )

                        listItems.add(Divider.Item())

                        listItems.add(txnList.getGraphItem())

                        listItems.add(Divider.Item())

                        listItems
                    }
                    .flatMap {
                        listItems ->
                        getDataManager()
                                .getAllCategories()
                                .map { it.toList().filter { it.type == TransactionType.DEBIT } }
                                .map {
                                    listItems.addAll(txnList.getTxnListItems(it))
                                    listItems
                                }
                    }

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
                                    "TODAY"
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
                            R.color.green,
                            R.color.lightGreen
                    )
            )
        }

        private fun List<Transaction>.getTxnListItems(categories: List<Category>)
                : List<ListItem<*, *>> {

            if(this.isEmpty()) {
                return mutableListOf()
            }

            categoriesMap = categories.groupBy { it.id }.mapValues { it.value[0] }

            val list = mutableListOf<ListItem<*, *>>()

            list.add(StatHeader.Item(StatHeader("TRANSACTIONS")))

            val inputSorted =
                    this
                            .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                            .reversed()

            var date = ""
            var i = 0

            while (i < inputSorted.size) {

                if (date != inputSorted[i].date) {

                    list.add(
                            DateHeaderListItem(
                                    Util.beautifyDate(inputSorted[i].date).toUpperCase(),
                                    R.color.green,
                                    R.color.lightGreen,
                                    date == ""
                            )
                    )

                    date = inputSorted[i].date
                }

                list.add(
                        TransactionItem.Item(
                                TransactionItem(
                                        inputSorted[i],
                                        categoriesMap[inputSorted[i].categoryId]!!,
                                        i == inputSorted.size - 1
                                )
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
                                transaction, categoriesMap[transaction.categoryId]!!
                        )
                    }
                }

        override fun onTransactionEdited() {
            fetchItems()
        }
    }
}