package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.Spreadsheet
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.SpreadsheetState
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.SpreadsheetDao
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class RoomDataManagerImpl
@Inject constructor(private val spreadsheetDao: SpreadsheetDao): SqliteDataManager {

    override fun getSpreadsheetIdForEmail(email: String) =
            Observable.fromCallable { spreadsheetDao.findByEmail(email)?.spreadsheetId ?: "" }!!

    override fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String) =

            Completable.fromCallable {

                var spreadsheet: Spreadsheet? = spreadsheetDao.findByEmail(email)

                if (spreadsheet == null) {

                    // if null insert
                    spreadsheet = Spreadsheet(email, spreadsheetId, SpreadsheetState.CREATED)
                    spreadsheetDao.insertAll(spreadsheet)

                } else {

                    // else update
                    spreadsheet.spreadsheetId = spreadsheetId
                    spreadsheetDao.updateAll(spreadsheet)

                }

            }!!

    @Throws(SqliteDataManager.SpreadsheetDoesNotExistException::class)
    override fun isSpreadsheetReady(email: String) =

            Observable.fromCallable {

                val spreadsheet: Spreadsheet? = spreadsheetDao.findByEmail(email)

                if (spreadsheet == null) {

                    throw SqliteDataManager.SpreadsheetDoesNotExistException(
                            "Spreadsheet does not exist $email."
                    )

                } else {
                    spreadsheet.state == SpreadsheetState.READY
                }

            }!!

    override fun setSpreadsheetReady(email: String) =

            Completable.fromCallable {

                val spreadsheet: Spreadsheet? = spreadsheetDao.findByEmail(email)

                if (spreadsheet == null) {

                    throw SqliteDataManager.SpreadsheetDoesNotExistException(
                            "Spreadsheet does not exist $email."
                    )

                } else {
                    spreadsheet.state = SpreadsheetState.READY
                    spreadsheetDao.updateAll(spreadsheet)
                }

            }!!
}