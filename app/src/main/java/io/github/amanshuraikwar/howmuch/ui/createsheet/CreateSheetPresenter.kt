package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.accounts.Account
import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateSheetPresenter
    @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<CreateSheetContract.View>(appBus, dataManager), CreateSheetContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)
    }

    override fun onCreateSheetClicked() {
        createSpreadSheet()
    }

    private fun createSpreadSheet() {
        getDataManager().getAuthenticationManager().let {

            authMan ->

            Log.d(TAG, "onAttach:checking permissions")

            if (authMan.hasPermissions()) {

                Log.d(TAG, "onAttach:has permissions")

                authMan.getLastSignedAccount()!!.account!!.let {

                    account ->

                    Log.d(TAG, "onAttach:account is not null")

                    val spreadSheetTitle = "HowMuch-${Util.getCurMonth()}-${Util.getCurDateTime()}"
                    val sheetTitles = listOf("Metadata", "Transactions")

                    val categoriesSpreadSheetRange = "Metadata!B2:B"
                    val transactionsSpreadSheetRange = "Transactions!B2:F"

                    createSheetObservable(spreadSheetTitle, sheetTitles, account)
                            .doOnSubscribe{
                                getView()?.updateLoading("Creating new spreadsheet...")
                            }
                            .map { saveToSharedPrefs("spread-sheet-id", it) }
                            .flatMap {
                                id ->
                                appendToSpreadSheetObservable(
                                        id,
                                        categoriesSpreadSheetRange,
                                        "RAW",
                                        listOf(
                                                listOf("Categories"),
                                                listOf("Food"),
                                                listOf("Travel")
                                        ),
                                        account)
                                        .doOnSubscribe {
                                            getView()?.updateLoading("Populating default categories...")
                                        }
                            }
                            .flatMap {
                                id ->
                                appendToSpreadSheetObservable(
                                        id,
                                        transactionsSpreadSheetRange,
                                        "RAW",
                                        listOf(
                                                listOf("Transactions"),
                                                listOf("Date", "Time", "Amount", "Description", "Category")
                                        ),
                                        account)
                                        .doOnSubscribe {
                                            getView()?.updateLoading("Populating transaction headings...")
                                        }
                            }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        Log.d(TAG, "createSpreadSheet: onNext: id = $it")
                                        getView()?.run {
                                            showSnackBar("Spread sheet created.")
                                            showId("ID: $it")
                                            showName("TITLE: $spreadSheetTitle")
                                        }
                                    },
                                    {
                                        getView()?.run {
                                            showSnackBar("Could not create a new spreadsheet!")
                                            showProceedButton()
                                            hideCreateSheetButton()
                                            hideLoading()
                                        }
                                    },
                                    {
                                        getView()?.run {
                                            showProceedButton()
                                            hideCreateSheetButton()
                                            hideLoading()
                                        }
                                    },
                                    {
                                        getView()?.run {
                                            hideProceedButton()
                                            hideCreateSheetButton()
                                            showLoading()
                                        }
                                    }
                            )
                }
            }
        }
    }

    private fun createSheetObservable(spreadSheetTitle: String,
                                      sheetTitles: List<String>,
                                      googleAccount: Account)
            =
            getDataManager()
                    .createSpreadSheet(
                        spreadSheetTitle,
                        sheetTitles,
                        getView()!!.getGoogleAccountCredential(googleAccount)
                    )

    private fun saveToSharedPrefs(key: String, value: String): String {
        return value
    }

    private fun appendToSpreadSheetObservable(spreadSheetId: String,
                                              spreadsheetRange : String,
                                              valueInputOption: String,
                                              values: List<List<Any>>,
                                              googleAccount: Account)
            =
            getDataManager()
                    .appendToSpreadSheet(
                            spreadSheetId,
                            spreadsheetRange,
                            valueInputOption,
                            values,
                            getView()!!.getGoogleAccountCredential(googleAccount))
}