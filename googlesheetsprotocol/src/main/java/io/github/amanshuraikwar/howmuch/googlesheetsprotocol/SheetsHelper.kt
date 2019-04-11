package io.github.amanshuraikwar.howmuch.googlesheetsprotocol

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.Constants.WALLETS_START_ROW_WITHOUT_HEADING
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.*
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A helper class knowing the data manager.
 * Helps with the tasks dealing with data and a spreadsheet.
 *
 * @author Amanshu Raikwar
 */
class SheetsHelper @Inject constructor(private val sheetsDataSource: SheetsDataSource) {

    private val tag = Util.getTag(this)

    fun init(googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        return createNewSpreadSheet(googleAccountCredential)
                .flatMap {
                    initMetadata(it, googleAccountCredential)
                }
                .flatMap {
                    initTransactions(
                            spreadsheetId = it,
                            googleAccountCredential = googleAccountCredential
                    )
                }
    }

    private fun createNewSpreadSheet(googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        return sheetsDataSource.createSpreadSheet(
                spreadSheetTitle = createSpreadsheetTitle(),
                sheetTitles = getDefaultSheetTitles(),
                googleAccountCredential = googleAccountCredential
        )
    }


    private fun initMetadata(spreadsheetId: String,
                             googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        return sheetsDataSource
                .updateSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        Constants.METADATA_SHEET_TITLE +
                                "!" + Constants.CATEGORIES_CELL_RANGE_WITH_HEADING,
                        values = Constants.DEFAULT_CATEGORIES_WITH_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .flatMap {
                    sheetsDataSource
                            .updateSpreadSheet(
                                    spreadsheetId = spreadsheetId,
                                    spreadsheetRange =
                                    Constants.METADATA_SHEET_TITLE +
                                            "!" + Constants.WALLETS_CELL_RANGE_WITH_HEADING,
                                    values = Constants.DEFAULT_WALLETS_WITH_HEADING,
                                    googleAccountCredential = googleAccountCredential
                            )
                }
    }

    private fun initTransactions(spreadsheetId: String,
                                 sheetTitle: String = Constants.TRANSACTIONS_SHEET_TITLE,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<String> {

        return sheetsDataSource.updateSpreadSheet(
                spreadsheetId = spreadsheetId,
                spreadsheetRange =
                sheetTitle + "!" + Constants.TRANSACTIONS_CELL_RANGE_WITH_HEADING,
                values = Constants.TRANSACTIONS_HEADING,
                googleAccountCredential = googleAccountCredential
        )
    }

    fun fetchTransactions(spreadsheetId: String,
                          sheetTitle: String = Constants.TRANSACTIONS_SHEET_TITLE,
                          googleAccountCredential: GoogleAccountCredential)
            : Observable<List<Transaction>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        sheetTitle + "!" + Constants.TRANSACTIONS_CELL_RANGE_WITH_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { getTransactionList(it, sheetTitle) }
    }

    @Throws(InvalidTransactionEntryException::class)
    private fun List<Any>.toTransaction(cellPosition: Int,
                                        sheetTitle: String): Transaction {

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

        val cellRange = Util.getCellRange(
                sheetTitle,
                Constants.TRANSACTION_START_COL,
                Constants.TRANSACTION_END_COL,
                cellPosition
        )

        val amount: Double

        try {

            amount = this[2].toString().toDouble()

        } catch (e: NumberFormatException) {

            val errorMessage = "Transaction entry at $cellRange" +
                    " of sheet $sheetTitle" +
                    " has invalid amount ${this[2]}."

            Log.e(tag, "toTransaction: $errorMessage", e)

            throw InvalidTransactionEntryException(
                    errorMessage,
                    cellRange,
                    sheetTitle
            )
        }

        val type: TransactionType

        try {

           type = TransactionType.valueOf(this[6].toString())

        } catch (e: IllegalArgumentException) {

            val errorMessage = "Transaction entry at $cellRange" +
                    " of sheet $$sheetTitle" +
                    " has invalid type ${this[6]}."

            Log.e(tag, "toTransaction: $errorMessage", e)

            throw InvalidTransactionEntryException(
                    errorMessage,
                    cellRange,
                    sheetTitle
            )
        }

        return Transaction(
                id =
                "$sheetTitle!" +
                        "${Constants.TRANSACTION_START_COL}$cellPosition" +
                        ":${Constants.TRANSACTION_END_COL}",
                date = this[0].toString(),
                time = this[1].toString(),
                amount = amount,
                title = this[3].toString(),
                description = this[4].toString(),
                categoryId = this[5].toString(),
                type = type,
                walletId = this[7].toString()
        )
    }

    @Throws(InvalidTransactionEntryException::class)
    private fun getTransactionList(input: List<List<Any>>,
                                   sheetTitle: String): List<Transaction> {

        val startCellPosition = Util.getDefaultTransactionsSpreadSheetStartPosition() + 1

        // -1 because it's incremented before the value is used in the loop
        var count = -1

        var list = emptyList<Transaction>()

        if (input.size > 2) {
            list =  input.subList(2, input.size).map {
                count++
                it.toTransaction(startCellPosition + count - 1, sheetTitle)

            }
        }

        return list
    }

    fun fetchCategories(spreadsheetId: String,
                        googleAccountCredential: GoogleAccountCredential)
            : Observable<Iterable<Category>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        "${Constants.METADATA_SHEET_TITLE}!" +
                                Constants.CATEGORIES_CELL_RANGE_WITHOUT_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { it.toCategories() }
    }

    @Throws(NoCategoriesFoundException::class)
    fun List<List<Any>>.toCategories(): Iterable<Category> {

        if (this.isEmpty()) {
            throw NoCategoriesFoundException("No categories found in the spread sheet.")
        }

        try {
            return this.map {
                Category(
                        id = it[0].toString(),
                        name = it[0].toString(),
                        type = TransactionType.valueOf(it[1].toString())
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw InvalidCategoryException("Invalid category found in the spread sheet.")
        }
    }

    fun fetchWallets(spreadsheetId: String,
                     googleAccountCredential: GoogleAccountCredential)
            : Observable<Iterable<Wallet>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        "${Constants.METADATA_SHEET_TITLE}!" +
                                Constants.WALLETS_CELL_RANGE_WITHOUT_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { it.toWallets() }
    }

    @Throws(NoWalletsFoundException::class, InvalidWalletException::class)
    fun List<List<Any>>.toWallets(): Iterable<Wallet> {

        if (this.isEmpty()) {
            throw NoWalletsFoundException("No wallets found in the spread sheet.")
        }

        var rowNum = WALLETS_START_ROW_WITHOUT_HEADING - 1

        try {
            return this.map {
                Wallet(
                        id = "${Constants.METADATA_SHEET_TITLE}!${Constants.WALLETS_START_COL}${++rowNum}:${Constants.WALLETS_END_COL}",
                        name = it[0].toString(),
                        balance = it[1].toString().toDouble()
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw InvalidWalletException("Invalid wallet found in the spread sheet.")
        } catch (e: NumberFormatException) {
            throw InvalidWalletException("Invalid wallet found in the spread sheet.")
        }
    }

    fun updateWallet(wallet: Wallet,
                     spreadsheetId: String,
                     googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .updateSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = wallet.id,
                        values =
                        listOf(
                                listOf(
                                        wallet.name,
                                        wallet.balance
                                )
                        ),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }

    fun addTransaction(transaction: Transaction,
                       spreadsheetId: String,
                       sheetTitle: String = Constants.TRANSACTIONS_SHEET_TITLE,
                       googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .appendToSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        "$sheetTitle!${Constants.TRANSACTIONS_CELL_RANGE_WITHOUT_HEADING}",
                        values =
                        listOf(
                                listOf(
                                        transaction.date,
                                        transaction.time,
                                        transaction.amount,
                                        transaction.title,
                                        // todo
                                        if (transaction.description == "") " " else transaction.description
                                                ?: " ",
                                        transaction.categoryId,
                                        transaction.type.toString(),
                                        // todo
                                        if (transaction.walletId == "") " " else transaction.walletId
                                )
                        ),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }

    fun updateTransaction(transaction: Transaction,
                          spreadsheetId: String,
                          googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .updateSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = transaction.id,
                        values =
                        listOf(
                                listOf(
                                        transaction.date,
                                        transaction.time,
                                        transaction.amount,
                                        transaction.title,
                                        // todo
                                        if (transaction.description == "") " " else transaction.description
                                                ?: " ",
                                        transaction.categoryId,
                                        transaction.type.toString(),
                                        // todo
                                        if (transaction.walletId == "") " " else transaction.walletId
                                )),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }


    fun deleteTransaction(transaction: Transaction,
                          spreadsheetId: String,
                          googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .deleteRows(
                        spreadsheetId = spreadsheetId,
                        sheetTitle = transaction.id.getSheetTitle(),
                        startIndex = Util.getRowNumber(transaction.id) - 1,
                        endIndex = Util.getRowNumber(transaction.id),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }

    private fun String.getSheetTitle(): String = this.split("!")[0]
    private fun String.getSheetRange(): String = this.split("!")[1]

    @Throws(SpreadSheetException::class)
    private fun getDefaultSheetTitles(): List<String> {

        return listOf(
                Constants.METADATA_SHEET_TITLE,
                Constants.TRANSACTIONS_SHEET_TITLE
        )
    }

    private fun createSpreadsheetTitle() = "HowMuch-" + Util.getCurDateTime()
}
