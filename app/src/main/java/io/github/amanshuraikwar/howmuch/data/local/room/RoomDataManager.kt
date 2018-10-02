package io.github.amanshuraikwar.howmuch.data.local.room

import io.reactivex.Completable
import io.reactivex.Observable

interface RoomDataManager {

    fun getSpreadsheetIdForYearAndMonth(year: Int, month: Int): Observable<String>

    /**
     * Adds a new entry if not exists, otherwise updates.
     */
    fun addSpreadsheetIdForYearAndMonth(spreadsheetId: String, year: Int, month: Int) : Completable
}