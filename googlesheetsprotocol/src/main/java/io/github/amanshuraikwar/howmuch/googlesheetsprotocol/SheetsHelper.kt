package io.github.amanshuraikwar.howmuch.googlesheetsprotocol

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.*
import io.github.amanshuraikwar.howmuch.protocol.*
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

    //region init

    // creates new spreadsheet & init metadata & transactions
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

    // creates a new spreadsheet & metadata and transaction sheet
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
                        spreadsheetRange = Constants.CATEGORIES_SPREAD_SHEET_RANGE_WITH_HEADING,
                        values = Constants.DEFAULT_CATEGORIES_WITH_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .flatMap {
                    sheetsDataSource
                            .updateSpreadSheet(
                                    spreadsheetId = spreadsheetId,
                                    spreadsheetRange =
                                    Constants.WALLETS_SPREAD_SHEET_RANGE_WITH_HEADING,
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
                spreadsheetRange = "$sheetTitle!${Constants.TRANSACTIONS_CELL_RANGE_WITH_HEADING}",
                values = Constants.TRANSACTIONS_HEADING,
                googleAccountCredential = googleAccountCredential
        )
    }
    //endregion

    //region transactions
    fun fetchTransactions(spreadsheetId: String,
                          sheetTitle: String = Constants.TRANSACTIONS_SHEET_TITLE,
                          googleAccountCredential: GoogleAccountCredential)
            : Observable<List<Transaction>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange =
                        "$sheetTitle!${Constants.TRANSACTIONS_CELL_RANGE_WITHOUT_HEADING}",
                        googleAccountCredential = googleAccountCredential
                )
                .map {
                    it.mapIndexed {
                        index, list ->
                        list.toTransaction(
                                Constants.TRANSACTION_START_ROW_WITHOUT_HEADING + index,
                                sheetTitle
                        )
                    }
                }
    }

    @Throws(InvalidTransactionEntryException::class)
    private fun List<Any>.toTransaction(cellPosition: Int,
                                        sheetTitle: String)
            : Transaction {

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
                                        if (transaction.description == "")
                                            " "
                                        else
                                            transaction.description ?: " ",
                                        transaction.categoryId,
                                        transaction.type.toString(),
                                        // todo
                                        if (transaction.walletId == "")
                                            " "
                                        else
                                            transaction.walletId
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
                                        if (transaction.description == "")
                                            " "
                                        else
                                            transaction.description ?: " ",
                                        transaction.categoryId,
                                        transaction.type.toString(),
                                        // todo
                                        if (transaction.walletId == "")
                                            " "
                                        else
                                            transaction.walletId
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
    //endregion

    //region categories
    fun fetchCategories(spreadsheetId: String,
                        googleAccountCredential: GoogleAccountCredential)
            : Observable<Iterable<Category>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = Constants.CATEGORIES_SPREAD_SHEET_RANGE_WITHOUT_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { it.toCategories() }
    }

    @Throws(NoCategoriesFoundException::class, InvalidCategoryException::class)
    private fun List<List<Any>>.toCategories()
            : Iterable<Category> {

        if (this.isEmpty()) {
            throw NoCategoriesFoundException("No categories found in the spread sheet.")
        }

        try {
            return this.mapIndexed {
                index, list ->
                Category(
                        id = "${Constants.METADATA_SHEET_TITLE}!${Constants.CATEGORIES_START_COL}${Constants.CATEGORIES_START_ROW_WITHOUT_HEADING + index}:${Constants.CATEGORIES_END_COL}",
                        name = list[0].toString(),
                        type = TransactionType.valueOf(list[1].toString()),
                        active = list[2].toString().toBoolean(),
                        monthlyLimit = list[3].toString().toDouble().money()
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw InvalidCategoryException("Invalid category found in the spread sheet.")
        }
    }

    fun addCategory(category: Category,
                    spreadsheetId: String,
                    googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .appendToSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = Constants.CATEGORIES_SPREAD_SHEET_RANGE_WITHOUT_HEADING,
                        values =
                        listOf(
                                listOf(
                                        category.name,
                                        category.type.toString(),
                                        category.active.toString()
                                )
                        ),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }

    fun updateCategory(category: Category,
                       spreadsheetId: String,
                       googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .updateSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = category.id,
                        values =
                        listOf(
                                listOf(
                                        category.name,
                                        category.type.toString(),
                                        category.active,
                                        category.monthlyLimit.toString()
                                )
                        ),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
    }
    //endregion

    //region wallets
    fun fetchWallets(spreadsheetId: String,
                     googleAccountCredential: GoogleAccountCredential)
            : Observable<Iterable<Wallet>> {

        return sheetsDataSource
                .readSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = Constants.WALLETS_SPREAD_SHEET_RANGE_WITHOUT_HEADING,
                        googleAccountCredential = googleAccountCredential
                )
                .map { it.toWallets() }
    }

    @Throws(NoWalletsFoundException::class, InvalidWalletException::class)
    private fun List<List<Any>>.toWallets(): Iterable<Wallet> {

        if (this.isEmpty()) {
            throw NoWalletsFoundException("No wallets found in the spread sheet.")
        }

        try {
            return this.mapIndexed {
                index, list ->
                Wallet(
                        id = "${Constants.METADATA_SHEET_TITLE}!${Constants.WALLETS_START_COL}${Constants.WALLETS_START_ROW_WITHOUT_HEADING + index}:${Constants.WALLETS_END_COL}",
                        name = list[0].toString(),
                        balance = list[1].toString().toDouble(),
                        active = list[2].toString().toBoolean()
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw InvalidWalletException("Invalid wallet found in the spread sheet.")
        } catch (e: NumberFormatException) {
            throw InvalidWalletException("Invalid wallet found in the spread sheet.")
        }
    }

    fun addWallet(wallet: Wallet,
                   spreadsheetId: String,
                   googleAccountCredential: GoogleAccountCredential)
            : Completable {

        return sheetsDataSource
                .appendToSpreadSheet(
                        spreadsheetId = spreadsheetId,
                        spreadsheetRange = Constants.WALLETS_SPREAD_SHEET_RANGE_WITHOUT_HEADING,
                        values =
                        listOf(
                                listOf(
                                        wallet.name,
                                        wallet.balance,
                                        wallet.active.toString()
                                )
                        ),
                        googleAccountCredential = googleAccountCredential
                )
                .ignoreElements()
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
    //endregion

    private fun String.getSheetTitle(): String = this.split("!")[0]
    private fun String.getSheetRange(): String = this.split("!")[1]

    @Throws(SpreadSheetException::class)
    private fun getDefaultSheetTitles()
            : List<String> {

        return listOf(
                Constants.METADATA_SHEET_TITLE,
                Constants.TRANSACTIONS_SHEET_TITLE
        )
    }

    private fun createSpreadsheetTitle() = "HowMuch-" + Util.getCurDateTime()
}
