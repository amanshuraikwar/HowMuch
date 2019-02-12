package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.accounts.Account
import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import java.lang.Exception
import javax.inject.Inject

class CreateSheetPresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager,
                        private val authMan: AuthenticationManager = dataManager.getAuthenticationManager())
    : BasePresenterImpl<CreateSheetContract.View>(appBus, dataManager), CreateSheetContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    // cashing values to avoid month-end edge cases
    private val curYear = Util.getCurYearNumber()
    private val curMonth = Util.getCurMonthNumber()

    private fun init() {

//        getDataManager().let {
//            dm ->
//            dm
//                    .getSpreadsheetIdForYearAndMonthAndEmail(curYear, curMonth, getEmail())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext {
//                        id ->
//                        Log.i(TAG, "init: getSpreadsheetIdForYearAndMonthAndEmail: onNext: id = $id")
//                        if (id == "") { // if id does not exists
//                            getView()?.showCreateSheetButton()
//                            getView()?.showHaveSpreadsheetButton()
//                        }
//                    }
//                    .observeOn(Schedulers.newThread())
//                    .filter { id -> id != "" } // if id exists
//                    .flatMap {
//                        dm
//                                .isSpreadsheetReady(curYear, curMonth, getEmail())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .doOnNext {
//                                    ready ->
//                                    Log.i(TAG, "init: isSpreadsheetReady: onNext: ready = $ready")
//                                    if (!ready) { // if sheet is not ready
//                                        getView()?.showCompleteSetupButton()
//                                    }
//                                }
//                                .observeOn(Schedulers.newThread())
//                    }
//                    .filter { ready -> ready } // if sheet is ready
//                    .flatMap {
//                        ready ->
//                        getDataManager()
//                                .setInitialOnboardingDone(true)
//                                .toSingleDefault(ready)
//                                .toObservable()
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnSubscribe {
//                        getView()?.run {
//                            hideCreateSheetButton()
//                            hideCompleteSetupButton()
//                            hideProceedButton()
//                            hideHaveSpreadsheetButton()
//                            hideCreateSheetInsteadButton()
//                        }
//                    }
//                    .subscribe(
//                            {
//                                getView()?.showProceedButton()
//                            },
//                            {
//                                it.printStackTrace()
//                                Log.e(TAG, it.toString())
//                                getView()?.showIndefiniteErrorSnackbar("Something went wrong!")
//                            },
//                            {
//                                Log.d(TAG, "init: chain end: onComplete: called")
//                            }
//                    )
//        }
    }

    override fun onCreateSheetClicked() {
        createNewSpreadsheet(getAccount()!!)
    }

    private fun createNewSpreadsheet(account: Account) {

//        val sheetTitles = Util.getDefaultSheetTitles()
//        val categories = Util.getDefaultCategoriesWithHeading()
//
//        val categoriesSpreadSheetRange = Util.getDefaultCategoriesSpreadSheetRangeWithHeading()
//        val transactionsSpreadSheetRange = Util.getDefaultTransactionsCellRangeWithHeading()
//
//        val defaultTransactionsHeading = Util.getDefaultTransactionsHeading()
//
//        getDataManager().let {
//            dm ->
//            dm
//                    .createSpreadSheet(
//                            spreadsheetTitle,
//                            sheetTitles,
//                            getView()!!.getGoogleAccountCredential(account)
//                    )
//                    .doOnSubscribe {
//                        getView()?.updateLoading("Creating new spreadsheet...")
//                    }
//                    .flatMap {
//                        id ->
//                        dm
//                                .addSpreadsheetIdForYearAndMonthAndEmail(
//                                        id,
//                                        curYear,
//                                        curMonth,
//                                        getEmail()
//                                )
//                                .doOnComplete {
//                                    Log.d(TAG, "createNewSpreadsheet: addSpreadsheetIdForYearAndMonthAndEmail: onComplete: called")
//                                }
//                                .toSingleDefault(id)
//                                .toObservable()
//                                .doOnNext {
//                                    Log.d(TAG, "createNewSpreadsheet: addSpreadsheetIdForYearAndMonthAndEmail: toSingleDefault: doOnNext: called")
//                                }
//                    }
//                    .flatMap {
//                        id ->
//                        updateSpreadSheetObservable(
//                                id,
//                                categoriesSpreadSheetRange,
//                                SheetsDataSource.VALUE_INPUT_OPTION,
//                                categories,
//                                account)
//                                .doOnSubscribe {
//                                    getView()?.updateLoading("Populating default categories...")
//                                }
//                    }
//                    .flatMap {
//                        id ->
//                        updateSpreadSheetObservable(
//                                id,
//                                transactionsSpreadSheetRange,
//                                SheetsDataSource.VALUE_INPUT_OPTION,
//                                defaultTransactionsHeading,
//                                account)
//                                .doOnSubscribe {
//                                    getView()?.updateLoading("Populating transaction headings...")
//                                }
//                    }
//                    .flatMap {
//                        id ->
//                        dm
//                                .setSpreadsheetReady(curYear, curMonth, getEmail())
//                                .toSingleDefault(id)
//                                .toObservable()
//                    }
//                    .flatMap {
//                        id ->
//                        dm
//                                .setInitialOnboardingDone(true)
//                                .toSingleDefault(id)
//                                .toObservable()
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                Log.d(TAG, "createSpreadSheet: onNext: id = $it")
//                                getView()?.run {
//                                    showSnackBar("Spread sheet created.")
//                                    showId("ID: $it")
//                                    showName("TITLE: $spreadsheetTitle")
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    showSnackBar("Something went wrong!")
//                                    hideLoading()
//                                    init()
//                                }
//                            },
//                            {
//                                getView()?.hideLoading()
//                                init()
//                            },
//                            {
//                                getView()?.run {
//                                    hideCreateSheetButton()
//                                    hideHaveSpreadsheetButton()
//                                    showLoading()
//                                }
//                            }
//                    )
//        }
    }

    private fun updateSpreadSheetObservable(spreadSheetId: String,
                                            spreadsheetRange : String,
                                            valueInputOption: String,
                                            values: List<List<Any>>,
                                            googleAccount: Account)
            = null
//            getDataManager()
//                    .updateSpreadSheet(
//                            spreadSheetId,
//                            spreadsheetRange,
//                            valueInputOption,
//                            values,
//                            getView()!!.getGoogleAccountCredential(googleAccount))

    override fun onCompleteSetupClicked() {
        completeSetup(getAccount()!!)
    }

    private fun completeSetup(account: Account) {

//        val categories = Util.getDefaultCategoriesWithHeading()
//
//        val categoriesSpreadSheetRange = Util.getDefaultCategoriesSpreadSheetRangeWithHeading()
//        val transactionsSpreadSheetRange = Util.getDefaultTransactionsCellRangeWithHeading()
//
//        val defaultTransactionsHeading = Util.getDefaultTransactionsHeading()
//
//        getDataManager().let {
//            dm ->
//            dm
//                    .getSpreadsheetIdForYearAndMonthAndEmail(
//                            curYear,
//                            curMonth,
//                            getEmail()
//                    )
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext {
//                        id ->
//                        if (id == "") {
//                            getView()?.showSnackBar("Something went wrong, please retry.")
//                            getView()?.hideLoading()
//                            init()
//                        }
//                    }
//                    .observeOn(Schedulers.newThread())
//                    .filter { id -> id != "" }
//                    .flatMap {
//                        id ->
//                        updateSpreadSheetObservable(
//                                id,
//                                categoriesSpreadSheetRange,
//                                SheetsDataSource.VALUE_INPUT_OPTION,
//                                categories,
//                                account)
//                                .doOnSubscribe {
//                                    getView()?.updateLoading("Populating default categories...")
//                                }
//                    }
//                    .flatMap {
//                        id ->
//                        updateSpreadSheetObservable(
//                                id,
//                                transactionsSpreadSheetRange,
//                                SheetsDataSource.VALUE_INPUT_OPTION,
//                                defaultTransactionsHeading,
//                                account)
//                                .doOnSubscribe {
//                                    getView()?.updateLoading("Populating transaction headings...")
//                                }
//                    }
//                    .flatMap {
//                        id ->
//                        dm
//                                .setSpreadsheetReady(curYear, curMonth, getEmail())
//                                .toSingleDefault(id)
//                                .toObservable()
//                    }
//                    .flatMap {
//                        id ->
//                        dm
//                                .setInitialOnboardingDone(true)
//                                .toSingleDefault(id)
//                                .toObservable()
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                Log.d(TAG, "completeSetup: onNext: id = $it")
//                                getView()?.run {
//                                    showSnackBar("Setup complete.")
//                                    showId("ID: $it")
//                                }
//                            },
//                            {
//                                it.printStackTrace()
//                                Log.e(TAG, it.toString())
//                                getView()?.run {
//                                    showSnackBar("Something went wrong!")
//                                    hideLoading()
//                                    init()
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    hideLoading()
//                                    init()
//                                }
//                            },
//                            {
//                                getView()?.run {
//                                    hideCompleteSetupButton()
//                                    showLoading()
//                                }
//                            }
//                    )
//        }
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

    @Suppress("LiftReturnOrAssignment")
    private fun getAccount(): Account? {

        if (authMan.hasPermissions()) {

            val account = authMan.getLastSignedAccount()?.account

            if (account == null) {
                return null
            } else {
                return account
            }
        } else {
            return null
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getEmail(): String {

        if (authMan.hasPermissions()) {

            val email = authMan.getLastSignedAccount()?.email

            if (email == null) {
                return ""
            } else {
                return email
            }
        } else {
            return ""
        }
    }

    override fun createSheetInsteadClicked() {
        getView()?.run {
            hideCreateSheetInsteadButton()
            hideSaveIdButton()
            showHaveSpreadsheetButton()
            showCreateSheetButton()
            hideId()
        }
    }

    override fun haveSpreadsheetClicked() {
        getView()?.run {
            hideHaveSpreadsheetButton()
            hideCreateSheetButton()
            showCreateSheetInsteadButton()
            showSaveIdButton()
            showId("")
        }
    }

    override fun saveIdClicked(spreadsheetId: String) {
//        if (spreadsheetId == "") {
//            getView()?.showSpreadsheetIdError("Spreadsheet id cannot be empty!")
//        } else {
//            validateSpreadsheet(spreadsheetId, getAccount()!!)
//                    .flatMap {
//                        getDataManager()
//                                .addSpreadsheetIdForYearAndMonthAndEmail(
//                                        spreadsheetId, curYear, curMonth, getEmail()
//                                )
//                                .toSingleDefault(spreadsheetId)
//                                .toObservable()
//                    }
//                    .flatMap {
//                        getDataManager()
//                                .setSpreadsheetReady(
//                                        curYear, curMonth, getEmail()
//                                )
//                                .toSingleDefault(spreadsheetId)
//                                .toObservable()
//                    }
//                    .flatMap {
//                        getDataManager()
//                                .setInitialOnboardingDone(true)
//                                .toSingleDefault(spreadsheetId)
//                                .toObservable()
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                getView()?.hideLoading()
//                                onProceedClicked()
//                            },
//                            {
//                                it.printStackTrace()
//                                Log.e(TAG, it.toString())
//                                if (it is InvalidSpreadsheetException) {
//                                    getView()?.run {
//                                        showSnackBar("Invalid spreadsheet!")
//                                        hideLoading()
//                                        showSaveIdButton()
//                                        showCreateSheetInsteadButton()
//                                    }
//                                } else {
//                                    getView()?.run {
//                                        showSnackBar("Something went wrong!")
//                                        hideLoading()
//                                        showSaveIdButton()
//                                        showCreateSheetInsteadButton()
//                                    }
//                                }
//                            },
//                            {
//
//                            },
//                            {
//                                getView()?.run {
//                                    hideSaveIdButton()
//                                    hideCreateSheetInsteadButton()
//                                    showLoading()
//                                    updateLoading("Checking spreadsheet...")
//                                }
//                            }
//                    )
//        }
    }

//    private fun validateSpreadsheet(id: String, googleAccount: Account): Observable<String> {
//        return getDataManager()
//                .readSpreadSheet(
//                        id,
//                        Util.getDefaultCategoriesSpreadSheetRangeWithHeading(),
//                        getView()!!.getGoogleAccountCredential(googleAccount)
//                )
//                .flatMap {
//                    data ->
//                    Observable.fromCallable {
//
//                        if (data.size == 0) {
//                            throw InvalidSpreadsheetException("Metadata sheet is empty.")
//                        }
//
//                        if (data[0].size == 0) {
//                            throw InvalidSpreadsheetException("Categories heading is empty.")
//                        }
//
//                        if (data[0][0] != "Categories") {
//                            throw InvalidSpreadsheetException("Categories heading '${data[0][0]}' is invalid.")
//                        }
//
//                        if (data.size < 2) {
//                            throw InvalidSpreadsheetException("Metadata sheet has no categories.")
//                        }
//
//                        if (data[1].size == 0) {
//                            throw InvalidSpreadsheetException("Metadata sheet has no categories.")
//                        }
//
//                        id
//                    }
//                }
//                .flatMap {
//                    getDataManager()
//                            .readSpreadSheet(
//                                    id,
//                                    Util.getDefaultTransactionsCellRangeWithHeading(),
//                                    getView()!!.getGoogleAccountCredential(googleAccount)
//                            )
//                }
//                .flatMap {
//                    data ->
//                    Observable.fromCallable {
//
//                        if (data.size == 0) {
//                            throw InvalidSpreadsheetException("Transactions sheet is empty.")
//                        }
//
//                        if (data[0].size == 0) {
//                            throw InvalidSpreadsheetException("Transactions heading is empty.")
//                        }
//
//                        if (data[0][0] != "Transactions") {
//                            throw InvalidSpreadsheetException("Transactions heading is invalid.")
//                        }
//
//                        if (data.size < 2) {
//                            throw InvalidSpreadsheetException("Transactions sheet does not have content headings.")
//                        }
//
//                        if (data[1].size == 0) {
//                            throw InvalidSpreadsheetException("Transactions sheet content headings are empty.")
//                        }
//
//                        if (!(data[1][0] == "Date"
//                                        && data[1][1] == "Time"
//                                        && data[1][2] == "Amount"
//                                        && data[1][3] == "Description"
//                                        && data[1][4] == "Category")
//                        ) {
//                            throw InvalidSpreadsheetException("Invalid transaction content headings.")
//                        }
//
//                        id
//                    }
//                }
//    }

    class InvalidSpreadsheetException(message: String): Exception(message)
}