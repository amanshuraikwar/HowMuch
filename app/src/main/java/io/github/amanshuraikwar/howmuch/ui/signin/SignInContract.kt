package io.github.amanshuraikwar.howmuch.ui.signin

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SpreadSheetException
import io.github.amanshuraikwar.howmuch.ui.SheetsHelper
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.util.Util
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
        fun showProgress(progress: Int)
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
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        @Inject
        lateinit var sheetsHelper: SheetsHelper

        // extension function to get first name
        private fun String.firstName(): String = this.split(" ")[0]

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            getDataManager()
                    .isInitialOnboardingDone()
                    .flatMap {
                        onboardingDone ->
                        if (onboardingDone) {
                            Observable
                                    .just(onboardingDone)
                                    .delay(1000, TimeUnit.MILLISECONDS)
                        } else {
                            // if onboarding not done, delay for sometime
                            // for the (cool) auto layout animation
                            Observable
                                    .just(onboardingDone)
                                    .delay(1000, TimeUnit.MILLISECONDS)
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                onboardingDone ->
                                if (onboardingDone) {
                                    getAppBus().onBoardingComplete.onNext(Any())
                                } else {
                                    initSignIn()
                                }
                            },
                            {
                                // todo handle error scenario
                            }
                    )
                    .addToCleanup()
        }

        private fun initSignIn() {

            // if signed in just display account details
            if (isSignedIn()) {

                getView()?.run {
                    showSignInInfo()
                    hideSignInBtn()
                    showProceedBtn((getDisplayName() ?: Constants.DEFAULT_USER_NAME).firstName())
                    showGoogleUserInfo(
                            name = getDisplayName() ?: Constants.DEFAULT_USER_NAME,
                            email = getEmail() ?: Constants.DEFAULT_USER_EMAIL,
                            photoUrl = getPhotoUrl() ?: ""
                    )
                }

            } else {

                // else show button to sign in
                getView()?.run {
                    showSignInInfo()
                    hideProceedBtn()
                    showSignInBtn()
                    hideGoogleUserInfo()
                }
            }
        }

        override fun onSignInBtnClicked() {
            Log.d(tag, "onSignInBtnClicked: Initialing sign in.")
            getView()?.initiateSignIn()
        }

        override fun onProceedBtnClicked() {
            Log.d(tag, "onProceedBtnClicked: Starting onb onboarding process.")
            startOnboardingProcess()
        }

        private fun startOnboardingProcess() {

            getDataManager().let {

                dm ->
                dm
                        .getSpreadsheetIdForEmail(getEmail()!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            getView()?.run {
                                showLoading("Creating a google spreadsheet...")
                                showProgress(20)
                            }
                        }
                        .observeOn(Schedulers.newThread())
                        // creating spread sheet if not exists
                        .flatMap {
                            id ->
                            if (id == "") {
                                sheetsHelper
                                        .createNewSpreadSheet(
                                                getView()?.getGoogleAccountCredential(
                                                        getAccount()!!
                                                )!!
                                        )
                            } else {
                                Observable.just(id)
                            }
                        }
                        // checking if the spread sheet id is still empty
                        .map {
                            id ->
                            if (id == "") {
                                throw SpreadSheetException(
                                        "Spreadsheet id is still empty after creating/reading " +
                                                "from local cache."
                                )
                            } else {
                                id
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            getView()?.run {
                                showLoading("Validating the spreadsheet...")
                                showProgress(40)
                            }
                        }
                        .observeOn(Schedulers.newThread())
                        // checking validity of spread sheet id
                        .flatMap {
                            id ->
                            dm
                                    .isValidSpreadSheetId(
                                            spreadsheetId = id,
                                            googleAccountCredential = getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                    )
                                    .map {

                                        valid ->

                                        if (valid) {
                                            id
                                        } else {
                                            throw SpreadSheetException(
                                                    "Invalid spreadsheet id '$id'."
                                            )
                                        }
                                    }
                        }
                        // saving spreadsheet id to local db
                        .flatMap {
                            id ->
                            dm
                                    .addSpreadsheetIdForEmail(
                                            id,
                                            getEmail()!!
                                    )
                                    .toSingleDefault(id)
                                    .toObservable()
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            getView()?.run {
                                showLoading("Setting up the spreadsheet...")
                                showProgress(60)
                            }
                        }
                        .observeOn(Schedulers.newThread())
                        // populating up metadata
                        .flatMap {
                            id ->
                            sheetsHelper
                                    .initMetadata(
                                            id,
                                            getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                    )
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            getView()?.run {
                                showLoading("Almost there...")
                                showProgress(80)
                            }
                        }
                        .observeOn(Schedulers.newThread())
                        // populating up transactions
                        .flatMap {
                            id ->
                            sheetsHelper
                                    .initTransactions(
                                            spreadsheetId = id,
                                            googleAccountCredential = getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                    )
                        }
                        // setting spread sheet ready in local db
                        .flatMap {
                            id ->
                            dm
                                    .setSpreadsheetReady(getEmail()!!)
                                    .toSingleDefault(id)
                                    .toObservable()
                        }
                        // setting initial onboarding done in local db
                        .flatMap {
                            id ->
                            dm
                                    .setInitialOnboardingDone(true)
                                    .toSingleDefault(id)
                                    .toObservable()
                        }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            getView()?.run {
                                hideGoogleAccountEdit()
                                hideProceedBtn()
                                showLoading("Initiating...")
                                showProgress(0)
                            }
                        }
                        .subscribe(
                                {
                                    Log.d(
                                            tag,
                                            "startOnboardingProcess: onNext: Onboarding complete."
                                    )

                                    getView()?.run {
                                        hideLoading()
                                        showToast("Setup successful!")
                                    }

                                    // inform the system of onboarding complete
                                    getAppBus()
                                            .onBoardingComplete
                                            .onNext(Any())
                                },
                                {
                                    Log.e(tag, "startOnboardingProcess: onError: ", it)
                                    getView()?.run {
                                        showGoogleAccountEdit()
                                        showProceedBtn((getDisplayName() ?: Constants.DEFAULT_USER_NAME).firstName())
                                        hideLoading()
                                        showSnackbar("Something went wrong, please try again!")
                                    }
                                }
                        )
                        .addToCleanup()
            }
        }


        override fun onSignInResult(isSuccessful: Boolean) {

            if (isSuccessful) {

                getAppBus().signInSuccessful.onNext(Any())

                getView()?.run {
                    hideSignInBtn()
                    showProceedBtn((getDisplayName() ?: Constants.DEFAULT_USER_NAME).firstName())
                    showGoogleUserInfo(
                            name = getDisplayName() ?: Constants.DEFAULT_USER_NAME,
                            email = getEmail() ?: Constants.DEFAULT_USER_EMAIL,
                            photoUrl = getPhotoUrl() ?: "")
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

