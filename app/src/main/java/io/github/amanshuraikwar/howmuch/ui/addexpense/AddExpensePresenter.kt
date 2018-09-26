package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddExpensePresenter
    @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<AddExpenseContract.View>(appBus, dataManager), AddExpenseContract.Presenter {

    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            getCategories()
        }
    }

    private fun getCategories() {
        getDataManager().getAuthenticationManager().let {
            authMan ->
            Log.d(TAG, "onAttach:checking permissions")
            if (authMan.hasPermissions()) {
                Log.d(TAG, "onAttach:has permissions")
                if (authMan.getLastSignedAccount()?.account != null) {

                    Log.d(TAG, "onAttach:account is not null")

                    getDataManager()
                            .readSpreadSheet(
                                    "1HzV18zWmo_-LwuHT_WtXHvR18f7GwQj2EKNKq47iOIM"
                                    ,"Summary!B22:C",
                                    getView()!!
                                            .getGoogleAccountCredential(
                                                    authMan.getLastSignedAccount()!!.account!!))
                            .map { convertToCategoriesArray(it) }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        getView()?.populateCategories(it)
                                        getView()?.hideLoadingOverlay()
                                    },
                                    {
                                        getView()?.showErrorOverlay()
                                    },
                                    {

                                    },
                                    {
                                        getView()?.showLoadingOverlay()
                                    })
                }
            }
        }
    }

    private fun convertToCategoriesArray(input: MutableList<MutableList<Any>>): List<String> {
        val categories = mutableListOf<String>()
        input.forEach {
            categories.add(it[0].toString())
        }
        return categories
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

        getDataManager().getAuthenticationManager().let {
            authMan ->
            Log.d(TAG, "onAttach:checking permissions")
            if (authMan.hasPermissions()) {
                Log.d(TAG, "onAttach:has permissions")
                if (authMan.getLastSignedAccount()?.account != null) {

                    Log.d(TAG, "onAttach:account is not null")

                    getDataManager()
                            .appendToSpreadSheet(
                                    "1HzV18zWmo_-LwuHT_WtXHvR18f7GwQj2EKNKq47iOIM",
                                    "Transactions!B5:E",
                                    "RAW",
                                    listOf(listOf(expense.date, expense.amount, expense.description, expense.category)),
                                    getView()!!
                                            .getGoogleAccountCredential(
                                                    authMan.getLastSignedAccount()!!.account!!))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        if (it != null) {
                                            getView()?.showSnackBar("Added successfully!")
                                            getView()?.resetInputFields()
                                        } else {
                                            getView()?.showSnackBar("Could not add expense!")
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
        }
    }

    override fun onLoadingRetryClicked() {
        getCategories()
    }
}