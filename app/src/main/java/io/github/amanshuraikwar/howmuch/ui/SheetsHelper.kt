package io.github.amanshuraikwar.howmuch.ui

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.InvalidCategoryException
import io.github.amanshuraikwar.howmuch.data.network.sheets.InvalidTransactionEntryException
import io.github.amanshuraikwar.howmuch.data.network.sheets.NoCategoriesFoundException
import io.github.amanshuraikwar.howmuch.data.network.sheets.SpreadSheetException
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject

/**
 * A helper class knowing the data manager.
 * Helps with the tasks dealing with data and a spreadsheet.
 *
 * @author Amanshu Raikwar
 */
class SheetsHelper @Inject constructor(val dataManager: DataManager) {

    private val tag = Util.getTag(this)

    private var curYear = Util.getCurYearNumber()
    private var curMonth = Util.getCurMonthNumber()

    fun reset() {
        curYear = Util.getCurYearNumber()
        curMonth = Util.getCurMonthNumber()
    }

    fun createNewSpreadSheet(googleAccountCredential: GoogleAccountCredential) =
            dataManager.createSpreadSheet(
                    spreadSheetTitle = createSpreadsheetTitle(),
                    sheetTitles = getDefaultSheetTitles(curMonth, curYear),
                    googleAccountCredential = googleAccountCredential
            )

    fun initMetadata(spreadsheetId: String,
                     googleAccountCredential: GoogleAccountCredential) =
            dataManager.updateSpreadSheet(
                    spreadsheetId = spreadsheetId,
                    sheetTitle = Constants.METADATA_SHEET_TITLE,
                    spreadsheetRange = Constants.CATEGORIES_CELL_RANGE_WITH_HEADING,
                    values = Constants.DEFAULT_CATEGORIES_WITH_HEADING,
                    googleAccountCredential = googleAccountCredential
            )

    fun initTransactions(spreadsheetId: String,
                         googleAccountCredential: GoogleAccountCredential) =
            dataManager.updateSpreadSheet(
                    spreadsheetId = spreadsheetId,
                    sheetTitle = getSheetName(curMonth, curYear),
                    spreadsheetRange = Constants.TRANSACTIONS_CELL_RANGE_WITH_HEADING,
                    values = Constants.TRANSACTIONS_HEADING,
                    googleAccountCredential = googleAccountCredential
            )

    fun fetchTransactions(spreadsheetId: String,
                          sheetName: String,
                          googleAccountCredential: GoogleAccountCredential) =
            dataManager.readSpreadSheet(
                    spreadsheetId,
                    sheetName,
                    Constants.TRANSACTIONS_CELL_RANGE_WITH_HEADING,
                    googleAccountCredential
            ).map { getTransactionList(it) }!!

    @Throws(InvalidTransactionEntryException::class)
    fun List<Any>.toTransaction(cellPosition: Int): Transaction {

        if (this.size != Constants.TRANSACTION_ROW_COLUMN_COUNT) {

            val errorMessage = "Transaction entry at cell position $cellPosition" +
                    " has only ${this.size} column entries" +
                    " instead of ${Constants.TRANSACTION_ROW_COLUMN_COUNT}."

            Log.e(tag, "getTransactionList: $errorMessage")

            throw InvalidTransactionEntryException(
                    errorMessage,
                    cellPosition.toString(),
                    ""
            )
        }

        val txnMonth = Util.getMonthNumber(this[0].toString())
        val txnYear = Util.getYearNumber(this[0].toString())
        val txnSheetName = getSheetName(monthNumber = txnMonth, initialYear = txnYear)

        val cellRange = Util.getCellRange(
                txnSheetName,
                Constants.TRANSACTION_START_COL,
                Constants.TRANSACTION_END_COL,
                cellPosition
        )

        val amount: Double

        try {

            amount = this[2].toString().toDouble()

        } catch (e: NumberFormatException) {

            val errorMessage = "Transaction entry at $cellRange" +
                    " of sheet $txnSheetName" +
                    " has invalid amount ${this[2]}."

            Log.e(tag, "toTransaction: $errorMessage", e)

            throw InvalidTransactionEntryException(
                    errorMessage,
                    cellRange,
                    txnSheetName
            )
        }

        val type: TransactionType

        try {

           type = TransactionType.valueOf(this[6].toString())

        } catch (e: IllegalArgumentException) {

            val errorMessage = "Transaction entry at $cellRange" +
                    " of sheet $$txnSheetName" +
                    " has invalid type ${this[6]}."

            Log.e(tag, "toTransaction: $errorMessage", e)

            throw InvalidTransactionEntryException(
                    errorMessage,
                    cellRange,
                    txnSheetName
            )
        }

        return Transaction(
                id = "$txnMonth-$txnYear-$cellPosition",
                date = this[0].toString(),
                time = this[1].toString(),
                currency = dataManager.getCurrency(),
                amount = amount,
                title = this[3].toString(),
                description = this[4].toString(),
                category = this[5].toString(),
                type = type,
                cellRange = cellRange
        )
    }

    @Throws(InvalidTransactionEntryException::class)
    private fun getTransactionList(input: MutableList<MutableList<Any>>): List<Transaction> {

        val startCellPosition = Util.getDefaultTransactionsSpreadSheetStartPosition() + 1

        // -1 because it's incremented before the value is used in the loop
        var count = -1

        var list = emptyList<Transaction>()

        if (input.size > 2) {
            list =  input.subList(2, input.size).map {
                count++
                it.toTransaction(startCellPosition + count - 1)

            }
        }

        return list
    }

    private fun fetchDefaultCategories(spreadsheetId: String,
                                       googleAccountCredential: GoogleAccountCredential) =
            dataManager
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        sheetTitle = Constants.METADATA_SHEET_TITLE,
                        spreadsheetRange = Constants.CATEGORIES_CELL_RANGE_WITHOUT_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { it.toCategoriesSet() }!!

    fun fetchCategories(spreadSheetId: String,
                        googleAccountCredential: GoogleAccountCredential) =
            dataManager
                    .getCategories()
                    .map { it.toList() }
                    .flatMap {
                        categories ->
                        if (categories.isEmpty()) {
                                    fetchDefaultCategories(
                                            spreadSheetId,
                                            googleAccountCredential
                                    )
                                    .flatMap {
                                        fetchedCategories ->
                                        dataManager
                                                .setCategories(fetchedCategories)
                                                .toSingleDefault(fetchedCategories.toList())
                                                .toObservable()
                                    }
                        } else {
                            Observable.just(categories)
                        }
                    }!!

    fun addTransaction(transaction: Transaction,
                       spreadsheetId: String,
                       googleAccountCredential: GoogleAccountCredential) =
            dataManager
                    .appendToSpreadSheet(
                            spreadsheetId = spreadsheetId,
                            sheetTitle = getSheetName(curMonth, curYear),
                            spreadsheetRange = Constants.TRANSACTIONS_CELL_RANGE_WITHOUT_HEADING,
                            values = listOf(listOf(
                                    transaction.date,
                                    transaction.time,
                                    transaction.amount,
                                    transaction.title,
                                    transaction.description,
                                    transaction.category,
                                    transaction.type.toString()
                            )),
                            googleAccountCredential = googleAccountCredential
                    )
                    .ignoreElements()!!

    fun updateTransaction(transaction: Transaction,
                          spreadsheetId: String,
                          googleAccountCredential: GoogleAccountCredential) =
            dataManager
                    .updateSpreadSheet(
                            spreadsheetId = spreadsheetId,
                            sheetTitle = transaction.cellRange.getSheetTitle(),
                            spreadsheetRange = transaction.cellRange.getSheetRange(),
                            values = listOf(listOf(
                                    transaction.date,
                                    transaction.time,
                                    transaction.amount,
                                    transaction.title,
                                    transaction.description,
                                    transaction.category,
                                    transaction.type.toString()
                            )),
                            googleAccountCredential = googleAccountCredential
                    )
                    .ignoreElements()!!


    fun deleteTransaction(transaction: Transaction,
                          spreadsheetId: String,
                          googleAccountCredential: GoogleAccountCredential) =
            dataManager
                    .deleteRows(
                            spreadsheetId = spreadsheetId,
                            sheetTitle = transaction.cellRange.getSheetTitle(),
                            startIndex = Util.getRowNumber(transaction.cellRange) - 1,
                            endIndex = Util.getRowNumber(transaction.cellRange),
                            googleAccountCredential = googleAccountCredential
                    )
                    .ignoreElements()!!

    private fun String.getSheetTitle(): String = this.split("!")[0]
    private fun String.getSheetRange(): String = this.split("!")[1]

    @Throws(NoCategoriesFoundException::class)
    fun List<List<Any>>.toCategoriesSet(): Set<String> {

        if (this.isEmpty()) {
            throw NoCategoriesFoundException("No categories found in the spread sheet.")
        }

        try {
            return this.map { it -> it[0].toString() }.toSet()
        } catch (e: IndexOutOfBoundsException) {
            throw InvalidCategoryException("Invalid category found in the spread sheet.")
        }
    }

    @Throws(SpreadSheetException::class)
    private fun getSheetName(monthNumber: Int, initialYear: Int) =
        when (monthNumber) {
            1 -> "JANUARY"
            2 -> "FEBRUARY"
            3 -> "MARCH"
            4 -> "APRIL"
            5 -> "MAY"
            6 -> "JUNE"
            7 -> "JULY"
            8 -> "AUGUST"
            9 -> "SEPTEMBER"
            10 -> "OCTOBER"
            11 -> "NOVEMBER"
            12 -> "DECEMBER"
            else -> throw SpreadSheetException(
                    "Sheet name does not exist for month number $monthNumber."
            )
        }.plus("-$initialYear")

    @Throws(SpreadSheetException::class)
    private fun getDefaultSheetTitles(initialMonthNumber: Int, initialYear: Int) =
        Arrays.asList(
                Constants.METADATA_SHEET_TITLE,
                getSheetName(initialMonthNumber, initialYear)
        )

    private fun createSpreadsheetTitle() = "HowMuch-" + Util.getCurDateTime()
}
