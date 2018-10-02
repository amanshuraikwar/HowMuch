package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.accounts.Account
import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateSheetPresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager,
                        private val authMan: AuthenticationManager = dataManager.getAuthenticationManager())
    : BasePresenterImpl<CreateSheetContract.View>(appBus, dataManager), CreateSheetContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    private fun init() {

        getDataManager().let {
            dm ->
            dm
                    .getSpreadsheetIdForYearAndMonth( // get id for cur year & month
                            Util.getCurYearNumber(),
                            Util.getCurMonthNumber())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        id ->
                        Log.i(TAG, "init: getSpreadsheetIdForYearAndMonth: onNext: id = $id")
                        if (id == "") { // if id does not exists
                            getView()?.showCreateSheetButton()
                        }
                    }
                    .observeOn(Schedulers.newThread())
                    .filter { id -> id != "" } // if id exists
                    .flatMap {
                        dm
                                .isSpreadsheetReady()
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    ready ->
                                    Log.i(TAG, "init: isSpreadsheetReady: onNext: ready = $ready")
                                    if (!ready) { // if sheet is not ready
                                        getView()?.showCompleteSetupButton()
                                    }
                                }
                                .observeOn(Schedulers.newThread())
                    }
                    .filter { ready -> ready } // if sheet is ready
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run {
                            hideCreateSheetButton()
                            hideCompleteSetupButton()
                            hideProceedButton()
                        }
                    }
                    .subscribe(
                            {
                                getView()?.showProceedButton()
                            },
                            {
                                it.printStackTrace()
                                Log.e(TAG, it.toString())
                                getView()?.showIndefiniteErrorSnackbar("Something went wrong!")
                            },
                            {
                                Log.d(TAG, "init: chain end: onComplete: called")
                            }
                    )
        }
    }

    override fun onCreateSheetClicked() {
        createNewSpreadsheet(getAccount()!!)
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getAccount(): Account? {

        if (authMan.hasPermissions()) {

            val account = authMan.getLastSignedAccount()?.account

            if (account == null) {
                // todo invalid state
                return null
            } else {
                return account
            }
        } else {
            // todo invalid state
            return null
        }
    }

    private fun createNewSpreadsheet(account: Account) {

        val spreadsheetTitle = Util.createSpreadsheetTitle()
        val sheetTitles = Util.getDefaultSheetTitles()
        val categories = Util.getDefaultCategoriesWithHeading()

        val categoriesSpreadSheetRange = Util.getDefaultCategoriesSpreadSheetRangeWithHeading()
        val transactionsSpreadSheetRange = Util.getDefaultTransactionsSpreadSheetRangeWithHeading()

        val defaultTransactionsHeading = Util.getDefaultTransactionsHeading()

        getDataManager().let {
            dm ->
            dm
                    .createSpreadSheet(
                            spreadsheetTitle,
                            sheetTitles,
                            getView()!!.getGoogleAccountCredential(account)
                    )
                    .doOnSubscribe {
                        getView()?.updateLoading("Creating new spreadsheet...")
                    }
                    .flatMap {
                        id ->
                        dm
                                .addSpreadsheetIdForYearAndMonth(
                                        id,
                                        Util.getCurYearNumber(),
                                        Util.getCurMonthNumber()
                                )
                                .doOnComplete {
                                    Log.d(TAG, "createNewSpreadsheet: addSpreadsheetIdForYearAndMonth: onComplete: called")
                                }
                                .toSingleDefault(id)
                                .toObservable()
                                .doOnNext {
                                    Log.d(TAG, "createNewSpreadsheet: addSpreadsheetIdForYearAndMonth: toSingleDefault: doOnNext: called")
                                }
                    }
                    .flatMap {
                        id ->
                        updateSpreadSheetObservable(
                                id,
                                categoriesSpreadSheetRange,
                                SheetsDataSource.VALUE_INPUT_OPTION,
                                categories,
                                account)
                                .doOnSubscribe {
                                    getView()?.updateLoading("Populating default categories...")
                                }
                    }
                    .flatMap {
                        id ->
                        updateSpreadSheetObservable(
                                id,
                                transactionsSpreadSheetRange,
                                SheetsDataSource.VALUE_INPUT_OPTION,
                                defaultTransactionsHeading,
                                account)
                                .doOnSubscribe {
                                    getView()?.updateLoading("Populating transaction headings...")
                                }
                    }
                    .flatMap {
                        id ->
                        dm
                                .setSpreadsheetReady(true)
                                .toSingleDefault(id)
                                .toObservable()
                    }
                    .flatMap {
                        id ->
                        dm
                                .setInitialOnboardingDone(true)
                                .toSingleDefault(id)
                                .toObservable()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Log.d(TAG, "createSpreadSheet: onNext: id = $it")
                                getView()?.run {
                                    showSnackBar("Spread sheet created.")
                                    showId("ID: $it")
                                    showName("TITLE: $spreadsheetTitle")
                                }
                            },
                            {
                                getView()?.run {
                                    showSnackBar("Something went wrong!")
                                    hideLoading()
                                    init()
                                }
                            },
                            {
                                getView()?.hideLoading()
                                init()
                            },
                            {
                                getView()?.run {
                                    hideCreateSheetButton()
                                    showLoading()
                                }
                            }
                    )
        }
    }

    private fun updateSpreadSheetObservable(spreadSheetId: String,
                                            spreadsheetRange : String,
                                            valueInputOption: String,
                                            values: List<List<Any>>,
                                            googleAccount: Account)
            =
            getDataManager()
                    .updateSpreadSheet(
                            spreadSheetId,
                            spreadsheetRange,
                            valueInputOption,
                            values,
                            getView()!!.getGoogleAccountCredential(googleAccount))

    override fun onCompleteSetupClicked() {
        completeSetup(getAccount()!!)
    }

    private fun completeSetup(account: Account) {

        val categories = Util.getDefaultCategoriesWithHeading()

        val categoriesSpreadSheetRange = Util.getDefaultCategoriesSpreadSheetRangeWithHeading()
        val transactionsSpreadSheetRange = Util.getDefaultTransactionsSpreadSheetRangeWithHeading()

        val defaultTransactionsHeading = Util.getDefaultTransactionsHeading()

        getDataManager().let {
            dm ->
            dm
                    .getSpreadsheetIdForYearAndMonth(
                            Util.getCurYearNumber(),
                            Util.getCurMonthNumber()
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        id ->
                        if (id == "") {
                            getView()?.showSnackBar("Something went wrong, please retry.")
                            getView()?.hideLoading()
                            init()
                        }
                    }
                    .observeOn(Schedulers.newThread())
                    .filter { id -> id != "" }
                    .flatMap {
                        id ->
                        updateSpreadSheetObservable(
                                id,
                                categoriesSpreadSheetRange,
                                SheetsDataSource.VALUE_INPUT_OPTION,
                                categories,
                                account)
                                .doOnSubscribe {
                                    getView()?.updateLoading("Populating default categories...")
                                }
                    }
                    .flatMap {
                        id ->
                        updateSpreadSheetObservable(
                                id,
                                transactionsSpreadSheetRange,
                                SheetsDataSource.VALUE_INPUT_OPTION,
                                defaultTransactionsHeading,
                                account)
                                .doOnSubscribe {
                                    getView()?.updateLoading("Populating transaction headings...")
                                }
                    }
                    .flatMap {
                        id ->
                        dm
                                .setSpreadsheetReady(true)
                                .toSingleDefault(id)
                                .toObservable()
                    }
                    .flatMap {
                        id ->
                        dm
                                .setInitialOnboardingDone(true)
                                .toSingleDefault(id)
                                .toObservable()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Log.d(TAG, "completeSetup: onNext: id = $it")
                                getView()?.run {
                                    showSnackBar("Setup complete.")
                                    showId("ID: $it")
                                }
                            },
                            {
                                it.printStackTrace()
                                Log.e(TAG, it.toString())
                                getView()?.run {
                                    showSnackBar("Something went wrong!")
                                    hideLoading()
                                    init()
                                }
                            },
                            {
                                getView()?.run {
                                    hideLoading()
                                    init()
                                }
                            },
                            {
                                getView()?.run {
                                    hideCompleteSetupButton()
                                    showLoading()
                                }
                            }
                    )
        }
    }

    override fun onProceedClicked() {
        getAppBus().onBoardingScreenState.onNext(OnboardingScreen.State.CREATE_SHEET_COMPLETE)
    }

    override fun onIndefiniteRetryClicked() {
        init()
    }

    override fun onScreenSelected() {
        Log.d(TAG, "onScreenSelected: called")
        init()
    }
}