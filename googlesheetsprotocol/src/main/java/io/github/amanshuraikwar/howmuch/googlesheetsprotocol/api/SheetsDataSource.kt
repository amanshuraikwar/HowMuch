package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    companion object {
        const val VALUE_INPUT_OPTION = "RAW"
    }

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String,
                        googleAccountCredential: GoogleAccountCredential)
            : Observable<List<List<Any>>>

    fun appendToSpreadSheet(spreadsheetId : String,
                            spreadsheetRange : String,
                            valueInputOption: String = VALUE_INPUT_OPTION,                            values: List<List<Any>>,
                            googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun updateSpreadSheet(spreadsheetId : String,
                          spreadsheetRange : String,
                          valueInputOption: String = VALUE_INPUT_OPTION,
                          values: List<List<Any>>,
                          googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun createSpreadSheet(spreadSheetTitle: String,
                          sheetTitles: List<String>,
                          googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun deleteRows(spreadsheetId : String,
                   sheetTitle: String,
                   startIndex: Int,
                   endIndex: Int,
                   googleAccountCredential: GoogleAccountCredential): Observable<String>

    fun getSheetTitles(spreadsheetId : String,
                       googleAccountCredential: GoogleAccountCredential): Observable<List<String>>
}