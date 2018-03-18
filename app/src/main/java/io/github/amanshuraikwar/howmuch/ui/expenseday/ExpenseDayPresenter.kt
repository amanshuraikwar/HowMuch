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
import io.reactivex.Observable
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

            getAppBus()
                    .onTransactionsChanged
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                transaction ->
                                getView()?.let {
                                    if (Util.areSameDate(
                                                    transaction.getDateAdded(),
                                                    it.getCurDayExpense().date)) {
                                        updateEverything()
                                    }
                                }
                            },
                            {
                                // todo something on error
                            }
                    ).addToCleanup()
        }
    }

    override fun onEditTransactionClick(transaction: Transaction) {
        getView()?.startEditTransactionActivity(transaction)
    }

    override fun onDeleteTransactionClick(transaction: Transaction) {

        getDataManager()
                .deleteTransaction(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it) {
                                getAppBus().onTransactionsChanged.onNext(transaction)
                            }
                        },
                        {
                            // todo something on error
                        }
                ).addToCleanup()
    }

    private fun displayDate() {
        getView()?.getCurDayExpense()?.let {
            getView()?.displayDate(Util.formatDate(it.date))
        }
    }

    private fun updateEverything() {

        getView()?.getCurDayExpense()?.let {
            dayExpense ->
            getDataManager()
                    .getDayExpenseByDate(dayExpense.date)
                    .flatMap(this::getTransactionObs)
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

    private fun getTransactions() {

        getView()?.getCurDayExpense()?.let {
            getTransactionObs(it)
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

    private fun getTransactionObs(dayExpense: DayExpense)
            = getDataManager()
                    .getAllTransactionsForDate(dayExpense.date)
                    .map { transactions -> transactions.reversed() }
                    .zipWith(
                            getDataManager().getDailyLimitAmount(),
                            BiFunction {
                                t1: List<Transaction>, t2: Int ->
                                getTransactionListItems(t1, t2, dayExpense)
                            })

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