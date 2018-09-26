package io.github.amanshuraikwar.howmuch.data.network.sheets

import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>>

    fun appendToSpreadSheet(spreadsheetId : String,
                            spreadsheetRange : String,
                            valueInputOption: String,
                            values: List<List<Any>>): Observable<String>

    fun createSpreadSheet(spreadSheetTitle: String, sheetTitles: List<String>): Observable<String>
}