package io.github.amanshuraikwar.howmuch.data.local.room

import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class RoomDataManagerImpl
@Inject constructor(private val spreadsheetDao: SpreadsheetDao): SqliteDataManager {

    override fun getSpreadsheetIdForYearAndMonth(year: Int, month: Int): Observable<String> {
        return Observable.create {
            e ->
            e.onNext(spreadsheetDao.findByYearAndMonth(year, month)?.spreadsheetId ?: "")
            e.onComplete()
        }
    }

    override fun addSpreadsheetIdForYearAndMonth(spreadsheetId: String, year: Int, month: Int): Completable {

        return Completable.create {

            e ->

            var spreadsheet: Spreadsheet? = spreadsheetDao.findByYearAndMonth(year, month)

            if (spreadsheet == null) { // if null insert
                spreadsheet = Spreadsheet(year, month, spreadsheetId, SpreadsheetState.CREATED)
                spreadsheetDao.insertAll(spreadsheet)
            } else { // else update
                spreadsheet.spreadsheetId = spreadsheetId
                spreadsheetDao.updateAll(spreadsheet)
            }

            e.onComplete()
        }
    }

    @Throws(SqliteDataManager.SpreadsheetDoesNotExistException::class)
    override fun isSpreadsheetReady(year: Int, month: Int): Observable<Boolean> {

        return Observable.create {

            e ->

            val spreadsheet: Spreadsheet? = spreadsheetDao.findByYearAndMonth(year, month)

            if (spreadsheet == null) {
                throw SqliteDataManager
                        .SpreadsheetDoesNotExistException(
                                "Spreadsheet does not exists for year $year and month $month.")
            } else {
                e.onNext(spreadsheet.state == SpreadsheetState.READY)
            }
        }
    }

    @Throws(SqliteDataManager.SpreadsheetDoesNotExistException::class)
    override fun setSpreadsheetReady(year: Int, month: Int): Completable {

        return Completable.create {

            e ->

            val spreadsheet: Spreadsheet? = spreadsheetDao.findByYearAndMonth(year, month)

            if (spreadsheet == null) {
                throw SqliteDataManager
                        .SpreadsheetDoesNotExistException(
                                "Spreadsheet does not exists for year $year and month $month.")
            } else {
                spreadsheet.state = SpreadsheetState.READY
                spreadsheetDao.updateAll(spreadsheet)
                e.onComplete()
            }
        }
    }
}