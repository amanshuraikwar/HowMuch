package io.github.amanshuraikwar.howmuch.ui.expenseday

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.ui.list.ListItem

/**
 * Created by amanshuraikwar on 12/03/18.
 */
interface ExpenseDayContract {

    interface View : BaseView {
        fun displayDate(date: String)
        fun getCurDayExpense(): DayExpense
        fun displayDayTransactions(listItems: List<ListItem<*>>)
        fun getTransactionListItems(transactions: List<Transaction>,
                                    spentTodayAmount: Int,
                                    leftAmount: Int,
                                    aboveLimitAmount: Int): List<ListItem<*>>
        fun startEditTransactionActivity(transaction: Transaction)
    }

    interface Presenter : BasePresenter<View> {
        fun onEditTransactionClick(transaction: Transaction)
        fun onDeleteTransactionClick(transaction: Transaction)
    }
}