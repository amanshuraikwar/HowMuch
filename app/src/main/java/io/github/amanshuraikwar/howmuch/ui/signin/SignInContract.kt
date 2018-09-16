package io.github.amanshuraikwar.howmuch.ui.signin

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface SignInContract {

    interface View : BaseView {
        fun initiateSignIn()
        fun initiateSignOut()
        fun hideSignInBtn()
        fun showSignInBtn()
        fun hideSignOutBtn()
        fun showSignOutBtn()
        fun showToast(message: String)
        fun showSnackbar(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onSignInBtnClicked()
        fun onSignOutBtnClicked()
        fun onSignInResult(isSuccessful: Boolean)
    }
}