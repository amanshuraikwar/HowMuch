package io.github.amanshuraikwar.howmuch.ui.signin

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import javax.inject.Inject

class SignInPresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager,
                        private val authMan: AuthenticationManager = dataManager.getAuthenticationManager())
    : BasePresenterImpl<SignInContract.View>(appBus, dataManager), SignInContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    private var selected = false

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        // taking both in to consideration as this is the first screen
        // in some cases page is selected before the presenter is attached
        if (selected && wasViewRecreated) {
            init()
        }
    }

    private fun init() {

        Log.d(TAG, "init: called")

        val account = getAccount()

        // toggling sign in and sign out btns
        if (account != null) {

            getView()?.run {
                hideSignInBtn()
                showProceedBtn()
                showGoogleUserInfo(
                        email = account.email ?: "",
                        photoUrl = account.photoUrl?.toString() ?: "")
            }
        } else {

            getView()?.run {
                hideProceedBtn()
                showSignInBtn()
                hideGoogleUserInfo()
            }
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getAccount(): GoogleSignInAccount? {

        if (authMan.hasPermissions()) {

            val account = authMan.getLastSignedAccount()

            if (account == null) {
                return null
            } else {
                return account
            }
        } else {
            return null
        }
    }

    override fun onSignInBtnClicked() {
        getView()?.initiateSignIn()
    }

    override fun onProceedBtnClicked() {
        getAppBus().onBoardingScreenState.onNext(OnboardingScreen.State.SIGN_IN_COMPLETE)
    }

    override fun onSignInResult(isSuccessful: Boolean) {

        if (isSuccessful) {

            getAppBus().signInSuccessful.onNext(Any())

            getView()?.run {
                showSnackbar("Signed in successfully!")
                hideSignInBtn()
                showProceedBtn()
                showGoogleUserInfo(
                        email = getAccount()?.email ?: "",
                        photoUrl = getAccount()?.photoUrl?.toString() ?: "")
            }
        } else {
            getView()?.run {
                showSnackbar("Signed in failed!")
                showSignInBtn()
                hideProceedBtn()
                hideGoogleUserInfo()
            }
        }
    }

    override fun onEmailBtnClicked() {
        getView()?.run {
            initiateSignOut()
            hideProceedBtn()
            showSignInBtn()
            hideGoogleUserInfo()
            initiateSignIn()
        }
    }

    override fun onScreenSelected() {
        Log.d(TAG, "onScreenSelected: called")
        selected = true
    }
}