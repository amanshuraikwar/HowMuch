package io.github.amanshuraikwar.howmuch.data

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Implementation of the Data Manager for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
class DataManagerImpl @Inject constructor(
        @ApplicationContext private val context: Context,
        private val networkDataManager: NetworkDataManager,
        private val localDataManager: LocalDataManager) : DataManager {

    override fun getAllPhotos(page: Int, orderBy: String, perPage: Int)
            = networkDataManager.getAllPhotos(page, orderBy, perPage)

    override fun getAuthenticationManager()
            = networkDataManager.getAuthenticationManager()

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String)
            : Observable<MutableList<MutableList<Any>>>
            = networkDataManager.readSpreadSheet(spreadsheetId, spreadsheetRange)

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<MutableList<MutableList<Any>>>
            = networkDataManager.readSpreadSheet(spreadsheetId, spreadsheetRange, googleAccountCredential)

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>)
            : Observable<String>
            = networkDataManager.appendToSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values)


    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>,
                                     googleAccountCredential: GoogleAccountCredential)
            : Observable<String>
            = networkDataManager.appendToSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values, googleAccountCredential)

    override fun updateSpreadSheet(spreadsheetId: String,
                                   spreadsheetRange: String,
                                   valueInputOption: String,
                                   values: List<List<Any>>): Observable<String>
            = networkDataManager.updateSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values)

    override fun updateSpreadSheet(spreadsheetId: String,
                                   spreadsheetRange: String,
                                   valueInputOption: String,
                                   values: List<List<Any>>,
                                   googleAccountCredential: GoogleAccountCredential): Observable<String>
            = networkDataManager.updateSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values, googleAccountCredential)

    override fun createSpreadSheet(spreadSheetTitle: String, sheetTitles: List<String>)
            = networkDataManager.createSpreadSheet(spreadSheetTitle, sheetTitles)

    override fun createSpreadSheet(spreadSheetTitle: String,
                                   sheetTitles: List<String>,
                                   googleAccountCredential: GoogleAccountCredential)
            = networkDataManager.createSpreadSheet(spreadSheetTitle, sheetTitles, googleAccountCredential)

    override fun isInitialOnboardingDone() =
            localDataManager.isInitialOnboardingDone()

    override fun isSignedIn() =
            localDataManager.isSignedIn()

    override fun isSpreadsheetReady() =
            localDataManager.isSpreadsheetReady()

    override fun setInitialOnboardingDone(value: Boolean) =
            localDataManager.setInitialOnboardingDone(value)

    override fun setSignedIn(value: Boolean) =
            localDataManager.setSignedIn(value)

    override fun setSpreadsheetReady(value: Boolean) =
            localDataManager.setSpreadsheetReady(value)

    override fun getSpreadsheetIdForYearAndMonth(year: Int, month: Int) =
            localDataManager.getSpreadsheetIdForYearAndMonth(year, month)


    override fun addSpreadsheetIdForYearAndMonth(spreadsheetId: String, year: Int, month: Int) =
            localDataManager.addSpreadsheetIdForYearAndMonth(spreadsheetId, year, month)

    override fun isSpreadsheetReady(year: Int, month: Int): Observable<Boolean> {
        return localDataManager.isSpreadsheetReady(year, month)
    }

    override fun setSpreadsheetReady(year: Int, month: Int): Completable {
        return localDataManager.setSpreadsheetReady(year, month)
    }
}