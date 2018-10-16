package io.github.amanshuraikwar.howmuch.ui.history

import android.accounts.Account
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView
import io.github.amanshuraikwar.multiitemlistadapter.ListItem

interface HistoryContract {

    interface View : BaseView {
        fun getGoogleAccountCredential(account: Account): GoogleAccountCredential
        fun submitList(list: List<ListItem<*, *>>)
        fun showLoadingOverlay()
        fun hideLoadingOverlay()
        fun showErrorOverlay()
        fun showSnackBar(message: String)
        fun startExpenseActivity(expense: Expense)
    }

    interface Presenter : BasePresenter<View> {
        fun onLoadingRetryClicked()
    }
}