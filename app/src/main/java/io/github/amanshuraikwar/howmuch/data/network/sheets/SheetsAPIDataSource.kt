package io.github.amanshuraikwar.howmuch.data.network.sheets

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import com.google.api.services.sheets.v4.model.DimensionRange
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest




/**
 * @author Pedro Carrillo.
 * @author Amanshu Raikwar.
 */
class SheetsAPIDataSource(private val googleAccountCredential: GoogleAccountCredential,
                          private val transport : HttpTransport,
                          private val jsonFactory: JsonFactory) : SheetsDataSource {

    @Suppress("PrivatePropertyName")
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

        return Observable.fromCallable {

                    Log.d(TAG, "readSpreadSheet: executing")

                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues()
                }
    }

    @Suppress("UNUSED_VARIABLE")
    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>)
            : Observable<String> {

        Log.d(TAG, "appendToSpreadSheet:called")

        return Observable.fromCallable {

            Log.d(TAG, "appendToSpreadSheet: executing")

            val body = ValueRange().setValues(values)
            val response = sheetsAPI.spreadsheets().values()
                    .append(spreadsheetId, spreadsheetRange, body)
                    .setValueInputOption(valueInputOption)
                    .execute()
            spreadsheetId
        }
    }

    @Suppress("UNUSED_VARIABLE")
    override fun updateSpreadSheet(spreadsheetId: String,
                                   spreadsheetRange: String,
                                   valueInputOption: String,
                                   values: List<List<Any>>)
            : Observable<String> {

        Log.d(TAG, "updateSpreadSheet:called")

        return Observable.fromCallable {

            Log.d(TAG, "updateSpreadSheet: executing")

            val body = ValueRange().setValues(values)
            val response = sheetsAPI.spreadsheets().values()
                    .update(spreadsheetId, spreadsheetRange, body)
                    .setValueInputOption(valueInputOption)
                    .execute()
            spreadsheetId
        }
    }

    override fun createSpreadSheet(spreadSheetTitle: String, sheetTitles: List<String>): Observable<String> {

        Log.d(TAG, "createSpreadSheet: called")

        return Observable
                .fromCallable {

                    Log.d(TAG, "createSpreadSheet: executing")

                    val newSpreadSheet = Spreadsheet()
                    newSpreadSheet.properties = SpreadsheetProperties().setTitle(spreadSheetTitle)

                    val sheetProperties = SheetProperties()
                    sheetProperties.title = "hello hello"

                    val sheets = mutableListOf<Sheet>()

                    sheetTitles.forEach {
                        sheetTitle ->
                        val sheet = Sheet()
                        sheet.properties = SheetProperties().setTitle(sheetTitle)
                        sheets.add(sheet)
                    }

                    newSpreadSheet.sheets = sheets

                    val response =
                            sheetsAPI
                                    .spreadsheets()
                                    .create(newSpreadSheet)
                                    .execute()

                    Log.d(TAG, "createSpreadSheet: Spread sheet id created = " + response.spreadsheetId)

                    response.spreadsheetId
                }
    }

    override fun deleteRows(spreadsheetId: String,
                            sheetTitle: String,
                            startIndex: Int,
                            endIndex: Int): Observable<String> {

        Log.d(TAG, "deleteRows: called")

        return Observable.fromCallable {

            Log.d(TAG, "deleteRows: executing")

            val spreadsheet = sheetsAPI.spreadsheets().get(spreadsheetId).execute()

            val content = BatchUpdateSpreadsheetRequest()
            val request = Request()
            val deleteDimensionRequest = DeleteDimensionRequest()
            val dimensionRange = DimensionRange()
            dimensionRange.dimension = "ROWS"
            dimensionRange.startIndex = startIndex
            dimensionRange.endIndex = endIndex

            dimensionRange.sheetId =
                    spreadsheet
                            .sheets
                            .find { it.properties.title == sheetTitle }!!
                            .properties.sheetId

            deleteDimensionRequest.range = dimensionRange

            request.deleteDimension = deleteDimensionRequest

            content.requests = listOf(request)

            val response =
                    sheetsAPI.spreadsheets().batchUpdate(spreadsheetId, content).execute()

            spreadsheetId
        }
    }

    override fun getSheetTitles(spreadsheetId: String): Observable<List<String>> {

        Log.d(TAG, "getSheetTitles: executing")

        return Observable.fromCallable {
            sheetsAPI
                    .spreadsheets()
                    .get(spreadsheetId)
                    .execute()
                    .sheets
                    .map { it.properties.title }
        }
    }
}