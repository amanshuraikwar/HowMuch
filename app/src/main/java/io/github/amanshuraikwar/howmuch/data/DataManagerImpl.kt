package io.github.amanshuraikwar.howmuch.data

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManager
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

    override fun setInitialOnboardingDone(value: Boolean) =
            localDataManager.setInitialOnboardingDone(value)

    override fun setSignedIn(value: Boolean) =
            localDataManager.setSignedIn(value)

    override fun getSpreadsheetIdForYearAndMonthAndEmail(year: Int, month: Int, email: String) =
            localDataManager.getSpreadsheetIdForYearAndMonthAndEmail(year, month, email)


    override fun addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId: String, year: Int, month: Int, email: String) =
            localDataManager.addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId, year, month, email)

    override fun isSpreadsheetReady(year: Int, month: Int, email: String): Observable<Boolean> {
        return localDataManager.isSpreadsheetReady(year, month, email)
    }

    override fun setSpreadsheetReady(year: Int, month: Int, email: String): Completable {
        return localDataManager.setSpreadsheetReady(year, month, email)
    }

    override fun getCurrency() = localDataManager.getCurrency()

    override fun setCurrency(currency: String) {
        localDataManager.setCurrency(currency)
    }

    override fun getCategories() = localDataManager.getCategories()

    override fun setCategories(categories: Set<String>) =
            localDataManager.setCategories(categories)

    override fun deleteRows(spreadsheetId: String, sheetTitle: String, startIndex: Int, endIndex: Int): Observable<String> =
            networkDataManager.deleteRows(spreadsheetId, sheetTitle, startIndex, endIndex)

    override fun deleteRows(spreadsheetId: String, sheetTitle: String, startIndex: Int, endIndex: Int, googleAccountCredential: GoogleAccountCredential): Observable<String> =
            networkDataManager.deleteRows(spreadsheetId, sheetTitle, startIndex, endIndex, googleAccountCredential)
}