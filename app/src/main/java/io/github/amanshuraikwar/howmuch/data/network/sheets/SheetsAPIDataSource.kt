package io.github.amanshuraikwar.howmuch.data.network.sheets

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable

/**
* @author Pedro Carrillo.
 * @author Amanshu Raikwar.
*/
class SheetsAPIDataSource(private val googleAccountCredential: GoogleAccountCredential,
                          private val transport : HttpTransport,
                          private val jsonFactory: JsonFactory) : SheetsDataSource {

    private val TAG = Util.getTag(this)

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
        Log.d(TAG, "readSpreadSheet: called")
        return Observable
                .create{
                    Log.d(TAG, "readSpreadSheet: executing")
                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues()
                    it.onNext(response.getValues())}
    }

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>)
            : Observable<Boolean> {
        Log.d(TAG, "appendToSpreadSheet:called")
        return Observable.fromCallable {
            Log.d(TAG, "appendToSpreadSheet: executing")
            val body = ValueRange().setValues(values)
            val response = sheetsAPI.spreadsheets().values()
                    .append(spreadsheetId, spreadsheetRange, body)
                    .setValueInputOption(valueInputOption)
                    .execute()
            true
        }
    }
}