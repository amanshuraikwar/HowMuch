package io.github.amanshuraikwar.howmuch.ui.expenseday

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.RecyclerViewAdapter
import io.github.amanshuraikwar.howmuch.ui.list.bigexpense.BigExpenseListItem
import io.github.amanshuraikwar.howmuch.ui.list.header.HeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
import io.github.amanshuraikwar.howmuch.util.LogUtil
import kotlinx.android.synthetic.main.activity_expense_day.*

/**
 * Created by amanshuraikwar on 12/03/18.
 */
class ExpenseDayActivity
    : BaseActivity<ExpenseDayContract.View, ExpenseDayContract.Presenter>(), ExpenseDayContract.View {

    private val TAG = LogUtil.getLogTag(this)

    companion object {
        val KEY_DAY_EXPENSE = "day_expense"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_day)

        initUi()
    }

    private fun initUi() {
        transactionsRv.layoutManager = LinearLayoutManager(this)
    }

    override fun displayDate(date: String) {
        expenseDateTv.text = date
    }

    override fun getCurDayExpense()
            : DayExpense
            = intent.getParcelableExtra(KEY_DAY_EXPENSE)

    override fun displayDayTransactions(listItems: List<ListItem<*>>) {

        transactionsRv.adapter =
                RecyclerViewAdapter(
                        this,
                        ListItemTypeFactory(),
                        listItems)
    }

    override fun getTransactionListItems(transactions: List<Transaction>,
                                         spentTodayAmount: Int,
                                         leftAmount: Int,
                                         aboveLimitAmount: Int): List<ListItem<*>> {

        val maxTransactionAmount = getMaxTransactionAmount(transactions)
        val listItems = mutableListOf<ListItem<*>>()

        listItems.add(
                BigExpenseListItem(
                        getString(R.string.spent),
                        spentTodayAmount,
                        ContextCompat.getColor(this, R.color.blue),
                        ContextCompat.getColor(this, R.color.lightBlue),
                        ContextCompat.getColor(this, R.color.veryLightBlue)))

        if (aboveLimitAmount > 0) {

            listItems.add(
                    BigExpenseListItem(
                            getString(R.string.above_limit),
                            aboveLimitAmount,
                            ContextCompat.getColor(this, R.color.red),
                            ContextCompat.getColor(this, R.color.lightRed),
                            ContextCompat.getColor(this, R.color.veryLightRed)))
        } else {

            listItems.add(
                    BigExpenseListItem(
                            getString(R.string.left),
                            leftAmount,
                            ContextCompat.getColor(this, R.color.green),
                            ContextCompat.getColor(this, R.color.lightGreen),
                            ContextCompat.getColor(this, R.color.veryLightGreen)))
        }

        listItems.add(HeaderListItem(getString(R.string.transactions)))

        for (transaction in transactions) {
            listItems.add(TransactionListItem(transaction, maxTransactionAmount))
        }
        return listItems
    }

    private fun getMaxTransactionAmount(transactions: List<Transaction>): Int {
        var amount = 0
        for (transaction in transactions) {
            if (transaction.getAmount() > amount) {
                amount = transaction.getAmount()
            }
        }
        return amount
    }

}