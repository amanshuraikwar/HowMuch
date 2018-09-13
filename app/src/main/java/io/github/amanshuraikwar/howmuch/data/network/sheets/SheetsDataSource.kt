package io.github.amanshuraikwar.howmuch.data.network.sheets

import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>>
}