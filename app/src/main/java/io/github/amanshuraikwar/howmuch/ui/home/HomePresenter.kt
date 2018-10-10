package io.github.amanshuraikwar.howmuch.ui.home

import android.accounts.Account
import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.github.amanshuraikwar.howmuch.ui.base.AccountPresenter
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Implementation of presenter of HomeActivity.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
class HomePresenter @Inject constructor(appBus: AppBus,
                                        dataManager: DataManager)
    : AccountPresenter<HomeContract.View>(appBus, dataManager), HomeContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    //region parent class methods
    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            init()
            getAppBus()
                    .onLogout
                    .flatMap {
                        getDataManager()
                                .setInitialOnboardingDone(false)
                                .toSingleDefault(Any())
                                .toObservable()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Log.d(TAG, "onLogout: onNext: called")
                                init()
                                getView()?.hideBottomNav()
                            },
                            {

                            },
                            {
                                Log.d(TAG, "onLogout: onComplete: called")
                                init()
                                getView()?.hideBottomNav()
                            }
                    )
        }
    }
    //endregion

    private fun init() {

        val curYear = Util.getCurYearNumber()
        val curMonth = Util.getCurMonthNumber()
        val spreadSheetTitle = Util.createSpreadsheetTitle()

        getDataManager().let {
            dm ->
            dm
                    .isInitialOnboardingDone()
                    .doOnNext {
                        done ->
                        if (!done) { // if onboarding not done
                            getView()?.loadPage(NavigationPage.ONBOARDING)
                        }
                    }
                    .filter { done -> done }
                    .flatMap {
                        dm.getSpreadsheetIdForYearAndMonthAndEmail(
                                curYear, curMonth, getEmail()
                        )
                    }
                    .flatMap {
                        id ->
                        obsForSpreadsheetId(id, curYear, curMonth, spreadSheetTitle)
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                getView()?.setCurPage(0)
                                getView()?.loadPage(NavigationPage.ADD_EXPENSE)
                                getView()?.showBottomNav()
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                getView()?.showError(it.message ?: "Something went wrong!")
                            },
                            {
                                getView()?.hideLoading()
                            },
                            {
                                getView()?.showLoading()
                                getView()?.updateLoading("Hang on...")
                            }
                    )
        }

        getAppBus()
                .onBoardingScreenState
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    @Suppress("NON_EXHAUSTIVE_WHEN")
                    when(it) {
                        OnboardingScreen.State.ONBOARDING_COMPLETE -> {
                            getView()?.setCurPage(0)
                            getView()?.loadPage(NavigationPage.ADD_EXPENSE)
                            getView()?.showBottomNav()
                        }
                    }
                }
    }

    private fun obsForSpreadsheetId(curId: String,
                                    year: Int,
                                    month: Int,
                                    spreadSheetTitle: String): Observable<String>? {

        if (curId == "") {
            return createSpreadsheetObs(spreadSheetTitle)
                    .flatMap {
                        newId ->
                        getDataManager()
                                .addSpreadsheetIdForYearAndMonthAndEmail(newId, year, month, getEmail())
                                .toSingleDefault(newId)
                                .toObservable()
                    }
                    .flatMap {
                        newId ->
                        populateSpreadsheetObs(newId)
                    }
                    .flatMap {
                        newId ->
                        getDataManager()
                                .setSpreadsheetReady(year, month, getEmail())
                                .toSingleDefault(newId)
                                .toObservable()
                    }
        } else {
            return getDataManager()
                    .isSpreadsheetReady(year, month, getEmail())
                    .flatMap {
                        ready ->
                        obsForSpreadsheetReady(ready, curId, year, month)
                    }
        }
    }

    private fun obsForSpreadsheetReady(ready: Boolean,
                                       spreadSheetId: String,
                                       year: Int,
                                       month: Int): Observable<String> {

        return if (ready) {
            Observable.just(spreadSheetId)
        } else {
            populateSpreadsheetObs(spreadSheetId)
                    .flatMap {
                        _ ->
                        getDataManager()
                                .setSpreadsheetReady(year, month, getEmail())
                                .toSingleDefault(spreadSheetId)
                                .toObservable()
                    }
        }
    }

    private fun populateSpreadsheetObs(id: String): Observable<String> {

        return updateSpreadSheetObservable(
                id,
                Util.getDefaultCategoriesSpreadSheetRangeWithHeading(),
                SheetsDataSource.VALUE_INPUT_OPTION,
                Util.getDefaultCategoriesWithHeading(),
                getAccount()!!)
                .flatMap {
                    _ ->
                    updateSpreadSheetObservable(
                            id,
                            Util.getDefaultTransactionsSpreadSheetRangeWithHeading(),
                            SheetsDataSource.VALUE_INPUT_OPTION,
                            Util.getDefaultTransactionsHeading(),
                            getAccount()!!)
                }
    }

    private fun updateSpreadSheetObservable(spreadSheetId: String,
                                            spreadsheetRange : String,
                                            valueInputOption: String,
                                            values: List<List<Any>>,
                                            googleAccount: Account) =
            getDataManager()
                    .updateSpreadSheet(
                            spreadSheetId,
                            spreadsheetRange,
                            valueInputOption,
                            values,
                            getView()!!.getGoogleAccountCredential(googleAccount))

    private fun createSpreadsheetObs(spreadSheetTitle: String) =
            getDataManager()
                    .createSpreadSheet(
                            spreadSheetTitle,
                            Util.getDefaultSheetTitles(),
                            getView()!!.getGoogleAccountCredential(getAccount()!!)
                    )

    override fun onNavigationItemSelected(position: Int) {
        Log.d(TAG, "onNavigationItemSelected:called")
        Log.i(TAG, "onNavigationItemSelected: position = $position")
        when(position) {
            0 -> getView()?.loadPage(NavigationPage.ADD_EXPENSE)
            1 -> getView()?.loadPage(NavigationPage.HISTORY)
            2 -> getView()?.loadPage(NavigationPage.SETTINGS)
        }
    }

    override fun onRetryClicked() {
        init()
    }
}