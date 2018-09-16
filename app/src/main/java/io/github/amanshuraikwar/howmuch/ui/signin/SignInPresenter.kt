package io.github.amanshuraikwar.howmuch.ui.signin

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import javax.inject.Inject

class SignInPresenter
    @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<SignInContract.View>(appBus, dataManager), SignInContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        // toggling sign in and sign out btns
        if (getDataManager().getAuthenticationManager().hasPermissions()) {
            getView()?.run {
                hideSignInBtn()
                showSignOutBtn()
            }
        } else {
            getView()?.run {
                hideSignOutBtn()
                showSignInBtn()
            }
        }
    }

    override fun onSignInBtnClicked() {
        getView()?.initiateSignIn()
    }

    override fun onSignOutBtnClicked() {
        getView()?.run {
            initiateSignOut()
            hideSignOutBtn()
            showSignInBtn()
        }

    }

    override fun onSignInResult(isSuccessful: Boolean) {
        if (isSuccessful) {
            getAppBus().signInSuccessful.onNext(Any())
            getView()?.run {
                showToast("Signed in successfully!")
                hideSignInBtn()
                showSignOutBtn()
            }
        } else {
            getView()?.run {
                showSnackbar("Signed in failed! Click on sign in button to try again.")
            }
        }
    }
}