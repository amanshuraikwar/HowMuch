package io.github.amanshuraikwar.howmuch.ui.signin

import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignInPresenter
    @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<SignInContract.View>(appBus, dataManager), SignInContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    private var selected = false

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (selected && wasViewRecreated) {
            init()
        }
    }

    private fun init() {

        Log.d(TAG, "init: called")

        // toggling sign in and sign out btns
        if (getDataManager().getAuthenticationManager().hasPermissions()) {

            Log.d(TAG, "init: has permissions")

            val authMan = getDataManager().getAuthenticationManager()

            getView()?.run {
                hideSignInBtn()
                showNegBtn()
                showGoogleUserInfo(
                        email = authMan.getLastSignedAccount()?.email ?: "",
                        photoUrl = authMan.getLastSignedAccount()?.photoUrl?.toString() ?: "")
            }
        } else {

            Log.d(TAG, "init: does not have permissions")

            getView()?.run {
                hideNegBtn()
                showSignInBtn()
                hideGoogleUserInfo()
            }
        }
    }

    override fun onSignInBtnClicked() {
        getView()?.initiateSignIn()
    }

    override fun onNegBtnClicked() {
        getAppBus().onBoardingScreenState.onNext(OnboardingScreen.State.SIGN_IN_COMPLETE)
    }

    override fun onSignInResult(isSuccessful: Boolean) {
        if (isSuccessful) {
            getAppBus().signInSuccessful.onNext(Any())
            val authMan = getDataManager().getAuthenticationManager()
            getView()?.run {
                showToast("Signed in successfully!")
                hideSignInBtn()
                showNegBtn()
                showGoogleUserInfo(
                        email = authMan.getLastSignedAccount()?.email ?: "",
                        photoUrl = authMan.getLastSignedAccount()?.photoUrl?.toString() ?: "")
            }
        } else {
            getView()?.run {
                showSnackbar("Signed in failed! Click on sign in button to try again.")
            }
        }
    }

    override fun onEmailBtnClicked() {
        getView()?.run {
            initiateSignOut()
            hideNegBtn()
            showSignInBtn()
            hideGoogleUserInfo()
            getView()?.initiateSignIn()
        }
    }

    override fun onScreenSelected() {
        Log.d(TAG, "onScreenSelected: called")
        selected = true
    }
}