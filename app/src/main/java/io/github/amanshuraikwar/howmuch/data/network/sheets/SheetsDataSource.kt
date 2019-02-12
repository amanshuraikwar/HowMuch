package io.github.amanshuraikwar.howmuch.data.network.sheets

import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    companion object {
        const val VALUE_INPUT_OPTION = "RAW"
    }

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>>

    fun appendToSpreadSheet(spreadsheetId : String,
                            spreadsheetRange : String,
                            valueInputOption: String,
                            values: List<List<Any>>): Observable<String>

    fun updateSpreadSheet(spreadsheetId : String,
                          spreadsheetRange : String,
                          valueInputOption: String,
                          values: List<List<Any>>): Observable<String>

    fun createSpreadSheet(spreadSheetTitle: String, sheetTitles: List<String>): Observable<String>

    fun deleteRows(spreadsheetId : String,
                   sheetTitle: String,
                   startIndex: Int,
                   endIndex: Int): Observable<String>

    fun getSheetTitles(spreadsheetId : String): Observable<List<String>>
}