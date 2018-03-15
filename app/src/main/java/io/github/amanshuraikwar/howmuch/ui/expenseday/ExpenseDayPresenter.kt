package io.github.amanshuraikwar.howmuch.ui.expenseday

import android.support.v4.content.ContextCompat
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.home.HomeContract
import io.github.amanshuraikwar.howmuch.ui.list.ListItem
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 12/03/18.
 */
class ExpenseDayPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<ExpenseDayContract.View>(appBus, dataManager),
        ExpenseDayContract.Presenter {

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            displayDate()
            getTransactions()
        }
    }

    private fun displayDate() {
        getView()?.getCurDayExpense()?.let {
            getView()?.displayDate(Util.formatDate(it.date))
        }
    }

    private fun getTransactions() {

        getView()?.getCurDayExpense()?.let {
            getDataManager()
                .getAllTransactionsForDate(it.date)
                .map { transactions -> transactions.reversed() }
                .zipWith(
                        getDataManager().getDailyLimitAmount(),
                        BiFunction {
                            t1: List<Transaction>, t2: Int ->
                            getTransactionListItems(t1, t2, it)
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            listItems ->
                            when {
                                listItems != null -> getView()?.displayDayTransactions(listItems)
                            }
                        },
                        {

                        }
                ).addToCleanup()
        }
    }

    private fun getTransactionListItems(transactions: List<Transaction>,
                                        dailyLimitAmount: Int,
                                        dayExpense: DayExpense)
            : List<ListItem<*>> {

        val diff = dailyLimitAmount - dayExpense.amount

        return if (diff >= 0) {

                getView()!!.getTransactionListItems(
                        transactions,
                        dayExpense.amount,
                        diff,
                        0)
            } else {

                getView()!!.getTransactionListItems(
                        transactions,
                        dayExpense.amount,
                        0,
                        -diff)
            }
    }
}