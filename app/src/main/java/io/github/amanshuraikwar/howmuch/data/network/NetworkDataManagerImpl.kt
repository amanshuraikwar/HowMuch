package io.github.amanshuraikwar.howmuch.data.network

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsAPIDataSource
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Implementation of Network Data Manager.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
class NetworkDataManagerImpl
@Inject constructor(private val authenticationManager: AuthenticationManager,
                    private val httpTransport: HttpTransport,
                    private val jsonFactory: JacksonFactory)
    : NetworkDataManager {

    private var sheetsDataSource: SheetsDataSource? = null

    override fun getAuthenticationManager() = authenticationManager

    override fun updateSpreadSheet(spreadsheetId: String,
                                   sheetTitle: String,
                                   spreadsheetRange: String,
                                   values: List<List<Any>>,
                                   googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.updateSpreadSheet(
                spreadsheetId,
                "$sheetTitle!$spreadsheetRange",
                SheetsDataSource.VALUE_INPUT_OPTION,
                values
        )
    }

    override fun isValidSpreadSheetId(spreadsheetId: String,
                                      googleAccountCredential: GoogleAccountCredential)
            : Observable<Boolean> {
        // todo implement correctly
        return Observable.just(true)
    }

    override fun readSpreadSheet(spreadsheetId: String,
                                 sheetTitle: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<MutableList<MutableList<Any>>> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.readSpreadSheet(
                spreadsheetId,
                "$sheetTitle!$spreadsheetRange"
        )
    }

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     sheetTitle: String,
                                     spreadsheetRange: String,
                                     values: List<List<Any>>,
                                     googleAccountCredential: GoogleAccountCredential): Observable<String> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.appendToSpreadSheet(
                spreadsheetId, "$sheetTitle!$spreadsheetRange", SheetsDataSource.VALUE_INPUT_OPTION, values)
    }

    override fun createSpreadSheet(spreadSheetTitle: String, sheetTitles: List<String>, googleAccountCredential: GoogleAccountCredential): Observable<String> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.createSpreadSheet(spreadSheetTitle, sheetTitles)
    }

    override fun deleteRows(spreadsheetId: String, sheetTitle: String, startIndex: Int, endIndex: Int, googleAccountCredential: GoogleAccountCredential): Observable<String> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.deleteRows(spreadsheetId, sheetTitle, startIndex, endIndex)
    }

    override fun getSheetTitles(spreadsheetId: String, googleAccountCredential: GoogleAccountCredential): Observable<List<String>> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.getSheetTitles(spreadsheetId)
    }
}