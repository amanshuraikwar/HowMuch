package io.github.amanshuraikwar.howmuch.data.network

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.model.Photo
import io.reactivex.Observable

/**
 * Data Manager for the content fetched from the network.
 * This is the single entry point to fetch data from the network.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface NetworkDataManager {

    fun getAuthenticationManager(): AuthenticationManager


    fun readSpreadSheet(spreadsheetId: String,
                        sheetTitle: String,
                        spreadsheetRange: String,
                        googleAccountCredential: GoogleAccountCredential)
            : Observable<MutableList<MutableList<Any>>>


    fun updateSpreadSheet(spreadsheetId : String,
                          sheetTitle: String,
                          spreadsheetRange : String,
                          values: List<List<Any>>,
                          googleAccountCredential: GoogleAccountCredential)
            : Observable<String>


    fun appendToSpreadSheet(spreadsheetId : String,
                            sheetTitle: String,
                            spreadsheetRange : String,
                            values: List<List<Any>>,
                            googleAccountCredential: GoogleAccountCredential): Observable<String>


    fun createSpreadSheet(spreadSheetTitle: String,
                          sheetTitles: List<String>,
                          googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun isValidSpreadSheetId(spreadsheetId: String,
                             googleAccountCredential: GoogleAccountCredential): Observable<Boolean>

    fun deleteRows(spreadsheetId : String,
                   sheetTitle: String,
                   startIndex: Int,
                   endIndex: Int,
                   googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun getSheetTitles(spreadsheetId : String,
                       googleAccountCredential: GoogleAccountCredential): Observable<List<String>>
}