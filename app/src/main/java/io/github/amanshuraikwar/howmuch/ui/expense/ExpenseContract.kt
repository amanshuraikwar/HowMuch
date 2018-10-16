package io.github.amanshuraikwar.howmuch.ui.expense

import android.accounts.Account
import android.accounts.AuthenticatorDescription
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface ExpenseContract {

    interface View : BaseView {
        fun getCurExpense(): Expense
        fun getGoogleAccountCredential(account: Account): GoogleAccountCredential
        fun populateCategories(categories: List<String>)
        fun setSubmitBtnText(text: String)
        fun enableSubmitBtn()
        fun disableSubmitBtn()
        fun showSnackBar(message: String)
        fun showAmountError(message: String)
        fun showDescriptionError(message: String)
        fun initUi(date: String, amount: String, category: String, description: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onSubmitClicked(expense: Expense)
    }
}