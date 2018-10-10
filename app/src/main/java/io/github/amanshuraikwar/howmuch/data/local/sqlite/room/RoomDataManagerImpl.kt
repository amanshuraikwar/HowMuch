package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import io.github.amanshuraikwar.howmuch.data.local.sqlite.Spreadsheet
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SpreadsheetState
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class RoomDataManagerImpl
@Inject constructor(private val spreadsheetDao: SpreadsheetDao): SqliteDataManager {

    override fun getSpreadsheetIdForYearAndMonthAndEmail(year: Int,
                                                         month: Int,
                                                         email: String): Observable<String> {
        return Observable.create {
            e ->
            e.onNext(spreadsheetDao.findByYearAndMonthAndEmail(year, month, email)?.spreadsheetId ?: "")
            e.onComplete()
        }
    }

    override fun addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId: String,
                                                         year: Int,
                                                         month: Int,
                                                         email: String): Completable {

        return Completable.create {

            e ->

            var spreadsheet: Spreadsheet? =
                    spreadsheetDao.findByYearAndMonthAndEmail(year, month, email)

            if (spreadsheet == null) { // if null insert
                spreadsheet = Spreadsheet(year, month, email, spreadsheetId, SpreadsheetState.CREATED)
                spreadsheetDao.insertAll(spreadsheet)
            } else { // else update
                spreadsheet.spreadsheetId = spreadsheetId
                spreadsheetDao.updateAll(spreadsheet)
            }

            e.onComplete()
        }
    }

    @Throws(SqliteDataManager.SpreadsheetDoesNotExistException::class)
    override fun isSpreadsheetReady(year: Int, month: Int, email: String): Observable<Boolean> {

        return Observable.create {

            e ->

            val spreadsheet: Spreadsheet? =
                    spreadsheetDao.findByYearAndMonthAndEmail(year, month, email)

            if (spreadsheet == null) {
                throw SqliteDataManager.SpreadsheetDoesNotExistException(
                        "Spreadsheet does not exists for year $year and month $month.")
            } else {
                e.onNext(spreadsheet.state == SpreadsheetState.READY)
            }
        }
    }

    @Throws(SqliteDataManager.SpreadsheetDoesNotExistException::class)
    override fun setSpreadsheetReady(year: Int, month: Int, email: String): Completable {

        return Completable.create {

            e ->

            val spreadsheet: Spreadsheet? =
                    spreadsheetDao.findByYearAndMonthAndEmail(year, month, email)

            if (spreadsheet == null) {
                throw SqliteDataManager.SpreadsheetDoesNotExistException(
                        "Spreadsheet does not exists for year $year and month $month.")
            } else {
                spreadsheet.state = SpreadsheetState.READY
                spreadsheetDao.updateAll(spreadsheet)
                e.onComplete()
            }
        }
    }
}