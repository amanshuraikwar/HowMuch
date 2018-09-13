package io.github.amanshuraikwar.howmuch.data.network.sheets

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import io.reactivex.Observable

/**
* @author Pedro Carrillo.
 * @author Amanshu Raikwar.
*/
class SheetsAPIDataSource(private val googleAccountCredential: GoogleAccountCredential,
                          private val transport : HttpTransport,
                          private val jsonFactory: JsonFactory) : SheetsDataSource {


    private val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport,
                    jsonFactory,
                    googleAccountCredential)
                    .setApplicationName("test")
                    .build()
        }

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String): Observable<MutableList<MutableList<Any>>> {
        return Observable
                .fromCallable{
                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues() }
    }
}