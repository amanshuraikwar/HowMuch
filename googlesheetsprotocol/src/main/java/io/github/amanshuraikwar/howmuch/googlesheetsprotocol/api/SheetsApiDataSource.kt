package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import io.reactivex.Observable
import com.google.api.services.sheets.v4.model.DimensionRange
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import javax.inject.Inject

class SheetsApiDataSource
@Inject constructor(private val transport : HttpTransport,
                    private val jsonFactory: JsonFactory) : SheetsDataSource {

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<List<List<Any>>> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

        return Observable.fromCallable {

                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues() ?: listOf()
                }
    }

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>,
                                     googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

        return Observable.fromCallable {

            val body = ValueRange().setValues(values)
            sheetsAPI.spreadsheets().values()
                    .append(spreadsheetId, spreadsheetRange, body)
                    .setValueInputOption(valueInputOption)
                    .execute()
            spreadsheetId
        }
    }

    override fun updateSpreadSheet(spreadsheetId: String,
                                   spreadsheetRange: String,
                                   valueInputOption: String,
                                   values: List<List<Any>>,
                                   googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

        return Observable.fromCallable {


            val body = ValueRange().setValues(values)

            sheetsAPI
                    .spreadsheets()
                    .values()
                    .update(spreadsheetId, spreadsheetRange, body)
                    .setValueInputOption(valueInputOption)
                    .execute()

            spreadsheetId
        }
    }

    override fun createSpreadSheet(spreadSheetTitle: String,
                                   sheetTitles: List<String>,
                                   googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

        return Observable
                .fromCallable {

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

                    response.spreadsheetId
                }
    }

    override fun deleteRows(spreadsheetId: String,
                            sheetTitle: String,
                            startIndex: Int,
                            endIndex: Int,
                            googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

        return Observable.fromCallable {

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

            sheetsAPI.spreadsheets().batchUpdate(spreadsheetId, content).execute()

            spreadsheetId
        }
    }

    override fun getSheetTitles(spreadsheetId: String,
                                googleAccountCredential: GoogleAccountCredential)
            : Observable<List<String>> {

        val sheetsAPI =
                Sheets
                        .Builder(
                                transport,
                                jsonFactory,
                                googleAccountCredential
                        )
                        .setApplicationName("test")
                        .build()

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