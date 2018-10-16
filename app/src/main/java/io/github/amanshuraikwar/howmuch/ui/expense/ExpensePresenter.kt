package io.github.amanshuraikwar.howmuch.ui.expense

import android.accounts.Account
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.AccountPresenter
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExpensePresenter @Inject constructor(appBus: AppBus,
                                           dataManager: DataManager)
    : AccountPresenter<ExpenseContract.View>(appBus, dataManager), ExpenseContract.Presenter {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            getCategories()
        }
    }

    private fun getCategories() {

        getDataManager().let {
            dm ->
            dm
                    .getCategories()
                    .map { it.toList() }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        categoriesList ->
                        getView()?.populateCategories(categoriesList)
                        getView()?.getCurExpense()?.let {
                            getView()?.initUi(
                                    Util.beautifyDate(it.date),
                                    it.amount,
                                    it.category,
                                    it.description
                            )
                        }
                    }
        }
    }

    override fun onSubmitClicked(expense: Expense) {

        if (expense.amount == "") {
            getView()?.showAmountError("Can't be empty!")
            return
        }

        if (expense.description == "") {
            getView()?.showDescriptionError("Can't be empty!")
            return
        }

        updateExpense(expense, getAccount()!!, getEmail())
    }

    private fun updateExpense(expense: Expense, account: Account, email:String) {

        getDataManager().let {
            dm ->
            dm
                    .getSpreadsheetIdForYearAndMonthAndEmail(
                            Util.getCurYearNumber(),
                            Util.getCurMonthNumber(),
                            email
                    )
                    .flatMap {
                        id ->
                        dm
                                .updateSpreadSheet(
                                        id,
                                        expense.cellRange,
                                        SheetsDataSource.VALUE_INPUT_OPTION,
                                        listOf(
                                                listOf(
                                                        expense.date,
                                                        expense.time,
                                                        expense.amount,
                                                        expense.description,
                                                        expense.category
                                                )
                                        ),
                                        getView()!!.getGoogleAccountCredential(account)
                                )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                if (it != null) {
                                    getView()?.showSnackBar("Updated successfully!")
                                    getAppBus().onExpenseUpdated.onNext(expense)
                                } else {
                                    getView()?.showSnackBar("Could not update expense!")
                                }
                            },
                            {
                                getView()?.run {
                                    showSnackBar("Could not add expense!")
                                    enableSubmitBtn()
                                    setSubmitBtnText("save")
                                }
                            },
                            {
                                getView()?.run {
                                    enableSubmitBtn()
                                    setSubmitBtnText("save")
                                }
                            },
                            {
                                getView()?.run {
                                    disableSubmitBtn()
                                    setSubmitBtnText("saving...")
                                }
                            })
        }
    }

    override fun onDeleteClicked(expense: Expense) {
        deleteExpense(expense, getAccount()!!, getEmail())
    }

    private fun deleteExpense(expense: Expense, account: Account, email:String) {

        getDataManager().let {
            dm ->
            dm
                    .getSpreadsheetIdForYearAndMonthAndEmail(
                            Util.getCurYearNumber(),
                            Util.getCurMonthNumber(),
                            email
                    )
                    .flatMap {
                        id ->
                        dm
                                .deleteRows(
                                        id,
                                        Util.getDefaultTransactionsSheetTitle(),
                                        Util.getRowNumber(expense.cellRange) - 1,
                                        Util.getRowNumber(expense.cellRange),
                                        getView()!!.getGoogleAccountCredential(account)
                                )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                if (it != null) {
                                    getView()?.showSnackBar("Deleted successfully!")
                                    getView()?.close()
                                    getAppBus().onExpenseDeleted.onNext(expense)
                                } else {
                                    getView()?.showSnackBar("Could not delete expense!")
                                }
                            },
                            {
                                getView()?.run {
                                    showSnackBar("Could not delete expense!")
                                    enableSubmitBtn()
                                    setSubmitBtnText("save")
                                }
                            },
                            {
                                getView()?.run {
                                    enableSubmitBtn()
                                    setSubmitBtnText("save")
                                }
                            },
                            {
                                getView()?.run {
                                    disableSubmitBtn()
                                    setSubmitBtnText("deleting...")
                                }
                            })
        }
    }
}