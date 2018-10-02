package io.github.amanshuraikwar.howmuch.data.local.room

import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class RoomDataManagerImpl
@Inject constructor(private val spreadsheetDao: SpreadsheetDao): RoomDataManager {

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

            if (spreadsheet != null) {
                spreadsheet.spreadsheetId = spreadsheetId
                spreadsheetDao.updateAll(spreadsheet)
            } else {
                spreadsheet = Spreadsheet(year, month, spreadsheetId)
                spreadsheetDao.insertAll(spreadsheet)
            }

            e.onComplete()
        }
    }
}