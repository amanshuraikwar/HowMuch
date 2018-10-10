package io.github.amanshuraikwar.howmuch.data.local.sqlite

import io.reactivex.Completable
import io.reactivex.Observable
import java.lang.Exception

/**
 * Abstraction over local sqlite data manager.
 * Implementors should follow the definitions of the (to be) implemented methods as given below.
 * @author Amanshu Raikwar
 */
interface SqliteDataManager {

    /**
     * Gets spreadsheet id for given year, month and email.
     * @param year Year number.
     * @param month Month number.
     * @param email Email.
     * @return A rx observable of spreadsheet id. If not exists returns empty string.
     */
    fun getSpreadsheetIdForYearAndMonthAndEmail(year: Int, month: Int, email: String): Observable<String>

    /**
     * Adds a new entry if not exists, if exists updates.
     * @param spreadsheetId Spreadsheet id to be stored.
     * @param year Year number for which the spreadsheet id is.
     * @param month Month number for which the spreadsheet id is.
     * @param email Email for which the spreadsheet id is.
     * @return A rx completable.
     */
    fun addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId: String, year: Int, month: Int, email: String): Completable

    /**
     * Checks if spreadsheet for a given year, month and email is ready or not.
     * @param year Year number.
     * @param month Month number.
     * @param email Email.
     * @return A rx observable of boolean. Returns false if entry does not exists.
     * @throws SpreadsheetDoesNotExistException when spreadsheet does not exist.
     */
    @Throws(SpreadsheetDoesNotExistException::class)
    fun isSpreadsheetReady(year: Int, month: Int, email: String): Observable<Boolean>

    /**
     * Sets spreadsheet's state for a given year. month and email to ready.
     * @param year Year number.
     * @param month Month number.
     * @param email Email.
     * @return A rx completable.
     * @throws SpreadsheetDoesNotExistException when spreadsheet does not exist.
     */
    @Throws(SpreadsheetDoesNotExistException::class)
    fun setSpreadsheetReady(year: Int, month: Int, email: String): Completable

    /**
     * Exception to be thrown when an action is performed assuming spreadsheet will exist in DB
     * but actually it is nowhere to be seen in DB.
     * @param message Message related to the exception.
     * @author Amanshu Raikwar
     */
    class SpreadsheetDoesNotExistException(message: String) : Exception(message)
}