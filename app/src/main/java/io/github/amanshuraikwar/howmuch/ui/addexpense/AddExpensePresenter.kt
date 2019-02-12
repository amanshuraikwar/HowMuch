package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.accounts.Account
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.AccountPresenter
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddExpensePresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager)
    : AccountPresenter<AddExpenseContract.View>(appBus, dataManager), AddExpenseContract.Presenter {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

//        if (wasViewRecreated) {
//            getCategories(getAccount()!!, getEmail())
//        }
    }

    private fun getCategories(account: Account, email: String) {

//        getDataManager().let {
//            dm ->
//            dm
//                    .getCategories()
//                    .flatMap {
//                        categoriesSet ->
//                        if (categoriesSet.isEmpty()) {
//                            dm
//                                    .getSpreadsheetIdForYearAndMonthAndEmail(
//                                            Util.getCurYearNumber(),
//                                            Util.getCurMonthNumber(),
//                                            email
//                                    )
//                                    .flatMap {
//                                        id ->
//                                        dm
//                                                .readSpreadSheet(
//                                                        id,
//                                                        Util.getDefaultCategoriesSpreadSheetRange(),
//                                                        getView()!!.getGoogleAccountCredential(account)
//                                                )
//                                    }
//                                    .map { convertToCategoriesArray(it) }
//                                    .flatMap {
//                                        categoriesList ->
//                                        dm
//                                                .setCategories(categoriesList.toSet())
//                                                .toSingleDefault(categoriesList)
//                                                .toObservable()
//                                    }
//                        } else {
//                            Observable.just(categoriesSet.toList())
//                        }
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                getView()?.populateCategories(it)
//                                getView()?.hideLoadingOverlay()
//                            },
//                            {
//                                getView()?.showErrorOverlay()
//                            },
//                            {
//
//                            },
//                            {
//                                getView()?.showLoadingOverlay()
//                            })
//        }
    }

    private fun convertToCategoriesArray(input: MutableList<MutableList<Any>>): List<String> {
        val categories = mutableListOf<String>()
        input.forEach {
            categories.add(it[0].toString())
        }
        return categories
    }

    override fun onSubmitClicked(expense: Transaction) {

//        if (transaction.amount == "") {
//            getView()?.showAmountError("Can't be empty!")
//            return
//        }
//
//        if (transaction.description == "") {
//            getView()?.showDescriptionError("Can't be empty!")
//            return
//        }
//
//        addExpense(transaction, getAccount()!!, getEmail())
    }

//    private fun addExpense(transaction: Expense, account: Account, email:String) {

//        getDataManager().let {
//            dm ->
//            dm
//                    .getSpreadsheetIdForYearAndMonthAndEmail(
//                            Util.getCurYearNumber(),
//                            Util.getCurMonthNumber(),
//                            email
//                    )
//                    .flatMap {
//                        id ->
//                        dm
//                                .appendToSpreadSheet(
//                                        id,
//                                        Util.getDefaultTransactionsSpreadSheetRange(),
//                                        SheetsDataSource.VALUE_INPUT_OPTION,
//                                        listOf(
//                                                listOf(
//                                                        transaction.date,
//                                                        transaction.time,
//                                                        transaction.amount,
//                                                        transaction.description,
//                                                        transaction.category
//                                                )
//                                        ),
//                                        getView()!!.getGoogleAccountCredential(account)
//                                )
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                if (it != null) {
//                                    getView()?.showSnackBar("Added successfully!")
//                                    getView()?.resetInputFields()
//                                } else {
//                                    getView()?.showSnackBar("Could not add transaction!")
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    showSnackBar("Could not add transaction!")
//                                    enableSubmitBtn()
//                                    setSubmitBtnText("save")
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    enableSubmitBtn()
//                                    setSubmitBtnText("save")
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    disableSubmitBtn()
//                                    setSubmitBtnText("saving...")
//                                }
//                            })
//        }
//    }

    override fun onLoadingRetryClicked() {
//        getCategories(getAccount()!!, getEmail())
    }
}