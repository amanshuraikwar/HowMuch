package io.github.amanshuraikwar.howmuch.ui.home

import android.accounts.Account
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * MVP contract of HomeActivity.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface HomeContract {

    interface View : BaseView {
        fun setCurPage(index: Int)
        fun loadPage(navigationPage: NavigationPage)
        fun hideBottomNav()
        fun showBottomNav()
        fun hideMainFragmentContainer()
        fun showMainFragmentContainer()
        fun showToast(message: String)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun updateLoading(message: String)
        fun getGoogleAccountCredential(googleAccount: Account): GoogleAccountCredential
    }

    interface Presenter : BasePresenter<View> {
        fun onNavigationItemSelected(position: Int)
        fun onRetryClicked()
    }
}