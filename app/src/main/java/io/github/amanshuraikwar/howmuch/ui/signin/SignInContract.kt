package io.github.amanshuraikwar.howmuch.ui.signin

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.User
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util;
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface SignInContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun initiateSignIn()
        fun initiateSignOut()
        fun showSignInInfo()
        fun hideSignInBtn()
        fun showSignInBtn()
        fun hideProceedBtn()
        fun showProceedBtn(name: String)
        fun hideGoogleAccountEdit()
        fun showGoogleAccountEdit()
        fun showGoogleUserInfo(name: String, photoUrl: String, email: String)
        fun hideGoogleUserInfo()
    }

    interface Presenter : BasePresenter<View> {
        fun onSignInBtnClicked()
        fun onProceedBtnClicked()
        fun onSignInResult(isSuccessful: Boolean)
        fun onEditBtnClicked()
    }

    class SignInPresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private fun String.firstName(): String = this.split(" ")[0]

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            Observable
                    .timer(1, TimeUnit.SECONDS)
                    .flatMap {
                        getDataManager().getSignedInUser()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                user ->
                                getView()?.run {
                                    showSignInInfo()
                                    hideSignInBtn()
                                    showProceedBtn(user.name.firstName())
                                    showGoogleUserInfo(
                                            name = user.name,
                                            email = user.email,
                                            photoUrl = user.userPicUrl ?: ""
                                    )
                                }
                            },
                            {
                                getView()?.run {
                                    showSignInInfo()
                                    hideProceedBtn()
                                    showSignInBtn()
                                    hideGoogleUserInfo()
                                }
                            }
                    )
                    .addToCleanup()

        }

        override fun onSignInBtnClicked() {
            getView()?.initiateSignIn()
        }

        override fun onProceedBtnClicked() {
            startOnboardingProcess()
        }

        private fun startOnboardingProcess() {

            getDataManager()
                    .isSignedIn()
                    .flatMapCompletable {

                        signedIn ->

                        if (signedIn) {
                            Completable.complete()
                        } else {
                            getDataManager()
                                    .signIn(User("", "", "", ""))
                                    .ignoreElements()
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run {
                            hideGoogleAccountEdit()
                            hideProceedBtn()
                            showLoading("Signing in...")
                        }
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideLoading()
                                }

                                // inform the system of onboarding complete
                                getAppBus().onSignInSuccessful.onNext(Any())
                            },
                            {
                                Log.e(tag, "startOnboardingProcess: onError: ", it)

                                it.printStackTrace()

                                init()

                                getView()?.run {
                                    showSnackbar("Something went wrong, please try again!")
                                    hideLoading()
                                }
                            }
                    )
                    .addToCleanup()
        }


        override fun onSignInResult(isSuccessful: Boolean) {

            if (isSuccessful) {

                init()

            } else {

                getView()?.run {
                    showSnackbar("Signed in failed!")
                    showSignInBtn()
                    hideProceedBtn()
                    hideGoogleUserInfo()
                }

            }
        }

        override fun onEditBtnClicked() {
            getView()?.run {
                initiateSignOut()
                hideProceedBtn()
                showSignInBtn()
                hideGoogleUserInfo()
                initiateSignIn()
            }
        }
    }
}

