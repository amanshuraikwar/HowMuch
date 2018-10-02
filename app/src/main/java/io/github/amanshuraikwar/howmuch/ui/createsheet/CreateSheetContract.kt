package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.accounts.Account
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface CreateSheetContract {

    interface View : BaseView {
        fun getGoogleAccountCredential(account: Account): GoogleAccountCredential
        fun updateLoading(message: String)
        fun showLoading()
        fun hideLoading()
        fun showProceedButton()
        fun hideProceedButton()
        fun showCreateSheetButton()
        fun hideCreateSheetButton()
        fun showSnackBar(message: String)
        fun showId(id: String)
        fun hideId()
        fun showName(name: String)
        fun hideName()
        fun showCompleteSetupButton()
        fun hideCompleteSetupButton()
        fun showIndefiniteErrorSnackbar(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onCreateSheetClicked()
        fun onCompleteSetupClicked()
        fun onProceedClicked()
        fun onIndefiniteRetryClicked()
        fun onScreenSelected()
    }
}