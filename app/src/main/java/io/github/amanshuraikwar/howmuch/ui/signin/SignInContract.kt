package io.github.amanshuraikwar.howmuch.ui.signin

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface SignInContract {

    interface View : BaseView {
        fun initiateSignIn()
        fun initiateSignOut()
        fun hideSignInBtn()
        fun showSignInBtn()
        fun hideNegBtn()
        fun showNegBtn()
        fun showToast(message: String)
        fun showSnackbar(message: String)
        fun showGoogleUserInfo(photoUrl: String, email: String)
        fun hideGoogleUserInfo()
    }

    interface Presenter : BasePresenter<View> {
        fun onSignInBtnClicked()
        fun onNegBtnClicked()
        fun onSignInResult(isSuccessful: Boolean)
        fun onEmailBtnClicked()
        fun onScreenSelected()
    }
}