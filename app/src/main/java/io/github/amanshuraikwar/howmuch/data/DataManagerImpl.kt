package io.github.amanshuraikwar.howmuch.data

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManager
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

    override fun getAuthenticationManager() =
            networkDataManager.getAuthenticationManager()

    override fun readSpreadSheet(spreadsheetId: String,
                                 sheetTitle: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.readSpreadSheet(
                    spreadsheetId, sheetTitle, spreadsheetRange, googleAccountCredential
            )

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     sheetTitle: String,
                                     spreadsheetRange: String,
                                     values: List<List<Any>>,
                                     googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.appendToSpreadSheet(
                    spreadsheetId,
                    sheetTitle,
                    spreadsheetRange,
                    values,
                    googleAccountCredential
            )

    override fun updateSpreadSheet(spreadsheetId: String,
                                   sheetTitle: String,
                                   spreadsheetRange: String,
                                   values: List<List<Any>>,
                                   googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.updateSpreadSheet(
                    spreadsheetId,
                    sheetTitle,
                    spreadsheetRange,
                    values,
                    googleAccountCredential
            )

    override fun createSpreadSheet(spreadSheetTitle: String,
                                   sheetTitles: List<String>,
                                   googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.createSpreadSheet(
                    spreadSheetTitle, sheetTitles, googleAccountCredential
            )

    override fun isValidSpreadSheetId(spreadsheetId: String,
                                      googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.isValidSpreadSheetId(spreadsheetId, googleAccountCredential)

    override fun isInitialOnboardingDone() =
            localDataManager.isInitialOnboardingDone()


    override fun setInitialOnboardingDone(value: Boolean) =
            localDataManager.setInitialOnboardingDone(value)

    override fun getSpreadsheetIdForEmail(email: String) =
            localDataManager.getSpreadsheetIdForEmail(email)

    override fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String) =
            localDataManager.addSpreadsheetIdForEmail(spreadsheetId, email)

    override fun isSpreadsheetReady(email: String) =
            localDataManager.isSpreadsheetReady(email)

    override fun setSpreadsheetReady(email: String) =
            localDataManager.setSpreadsheetReady(email)

    override fun getCurrency() = localDataManager.getCurrency()

    override fun setCurrency(currency: String) = localDataManager.setCurrency(currency)

    override fun getCategories() = localDataManager.getCategories()

    override fun setCategories(categories: Set<String>) =
            localDataManager.setCategories(categories)

    override fun deleteRows(spreadsheetId: String,
                            sheetTitle: String,
                            startIndex: Int,
                            endIndex: Int,
                            googleAccountCredential: GoogleAccountCredential) =
            networkDataManager.deleteRows(
                    spreadsheetId, sheetTitle, startIndex, endIndex, googleAccountCredential
            )

    override fun getSheetTitles(spreadsheetId: String, googleAccountCredential: GoogleAccountCredential): Observable<List<String>> {
        return networkDataManager.getSheetTitles(spreadsheetId, googleAccountCredential)
    }
}