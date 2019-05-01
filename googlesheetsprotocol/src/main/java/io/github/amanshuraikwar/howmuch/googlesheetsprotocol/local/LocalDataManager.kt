package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local

import io.reactivex.Completable
import io.reactivex.Observable

interface LocalDataManager {

    /**
     * Gets spreadsheet id for given email.
     * @param email Email.
     * @return A rx observable of spreadsheet id. If not exists returns empty string.
     */
    fun getSpreadsheetIdForEmail(email: String): Observable<String>

    /**
     * Adds a new entry if not exists, if exists updates.
     * @param spreadsheetId Spreadsheet id to be stored.
     * @param email Email for which the spreadsheet id is.
     * @return A rx completable.
     */
    fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String): Observable<String>

    fun getMonthlyExpenseLimit(): Observable<Double>

    fun setMonthlyExpenseLimit(limit: Double): Completable
}
