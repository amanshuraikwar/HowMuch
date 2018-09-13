package io.github.amanshuraikwar.howmuch.ui.home

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
        fun loadPage(navigationPage: NavigationPage)
        fun hideBnv()
        fun showBnv()
        fun showSignInScreen()
        fun hideSignInScreen()
        fun hideMainFragmentContainer()
        fun showMainFragmentContainer()
        fun initiateSignIn()
        fun showToast(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onNavigationItemSelected(itemId: Int)
        fun onSignInBtnClicked()
        fun onSignInResult(isSuccessful: Boolean)
    }
}