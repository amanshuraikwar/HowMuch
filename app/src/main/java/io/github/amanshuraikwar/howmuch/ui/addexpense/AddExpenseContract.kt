package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.accounts.Account
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface AddExpenseContract {

    interface View : BaseView {
        fun getGoogleAccountCredential(account: Account): GoogleAccountCredential
        fun showToast(message: String)
        fun showLoadingOverlay()
        fun hideLoadingOverlay()
        fun populateCategories(categories: List<String>)
        fun showErrorOverlay()
        fun setSubmitBtnText(text: String)
        fun enableSubmitBtn()
        fun disableSubmitBtn()
        fun showSnackBar(message: String)
        fun showAmountError(message: String)
        fun showDescriptionError(message: String)
        fun resetInputFields()
    }

    interface Presenter : BasePresenter<View> {
        fun onSubmitClicked(expense: Transaction)
        fun onLoadingRetryClicked()
    }
}