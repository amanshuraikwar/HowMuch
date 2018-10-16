package io.github.amanshuraikwar.howmuch.ui.history

import android.accounts.Account
import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.AccountPresenter
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.date.DateListItem
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem
import io.github.amanshuraikwar.howmuch.ui.list.expense.ExpenseListItem
import io.github.amanshuraikwar.howmuch.ui.list.expense.ExpenseOnClickListener
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor(appBus: AppBus,
                                           dataManager: DataManager)
    : AccountPresenter<HistoryContract.View>(appBus, dataManager), HistoryContract.Presenter {

    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            getHistory(getAccount()!!)
        }
    }

    private fun getHistory(account: Account) {

        getDataManager().let {
            dm ->
            dm
                    .getSpreadsheetIdForYearAndMonthAndEmail(
                            Util.getCurYearNumber(),
                            Util.getCurMonthNumber(),
                            getEmail()
                    )
                    .flatMap {
                        id ->
                        dm
                                .readSpreadSheet(
                                        id,
                                        Util.getDefaultTransactionsSpreadSheetRange(),
                                        getView()!!.getGoogleAccountCredential(account)
                                )
                    }
                    .map { list -> getExpenseList(list) }
                    .map { processData(it) }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Log.d(TAG, "reading spread sheets: onNext")
                                getView()?.submitList(it)
                                getView()?.hideLoadingOverlay()
                            },
                            {
                                Log.d(TAG, "reading spread sheets: error thrown")
                                it.printStackTrace()
                                Log.e(TAG, it.message)
                                Log.e(TAG, it.toString())
                                getView()?.showErrorOverlay()
                            },
                            {
                                Log.d(TAG, "reading spread sheets: completed")
                            },
                            {
                                getView()?.showLoadingOverlay()
                                Log.d(TAG, "reading spread sheets: subscribed")
                            }
                    )

        }
    }

    private fun getExpenseList(input: MutableList<MutableList<Any>>): List<Expense> {

        val startCellPosition = Util.getDefaultTransactionsSpreadSheetStartPosition() + 1
        val currencySymbol = getDataManager().getCurrency()

        val list = mutableListOf<Expense>()
        var count = 0

        if (input.size > 1) {
            input.subList(1, input.size).forEach {
                list.add(
                        Expense(
                                id = count.toString(),
                                date = it[0].toString(),
                                time = Util.beautifyTime(it[1].toString()),
                                amount = currencySymbol+it[2].toString(),
                                description = it[3].toString(),
                                category = it[4].toString(),
                                cellRange = Util.getCellRange(
                                        Util.getDefaultTransactionsSheetTitle(),
                                        Util.getDefaultTransactionsSpreadSheetStartCol(),
                                        Util.getDefaultTransactionsSpreadSheetEndCol(),
                                        startCellPosition + count
                                )
                        )
                )
                count++
            }
        }
        return list
    }

    private fun processData(input: List<Expense>): List<ListItem<*, *>> {

        val inputSorted = input.sortedBy { it.date + it.time }.reversed()
        val list = mutableListOf<ListItem<*, *>>()

        val onClickListener = object : ExpenseOnClickListener {
            override fun onClick(expense: Expense) {
                expense.amount = expense.amount.substring(1)
                getView()?.startExpenseActivity(expense)
            }
        }

        var date = ""
        var i = 0
        while (i < inputSorted.size) {
            if (date != inputSorted[i].date) {
                date = inputSorted[i].date
                list.add(DateListItem(Util.beautifyDate(date)))
            }
            list.add(ExpenseListItem(inputSorted[i]).setOnClickListener(onClickListener))
            i += 1
        }
        if(list.size == 0) {
            list.add(EmptyListItem("No transactions!!\no_o"))
        } else {
            list.add(DateListItem(""))
        }

        return list
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: called")
    }

    override fun onLoadingRetryClicked() {
        getHistory(getAccount()!!)
    }
}