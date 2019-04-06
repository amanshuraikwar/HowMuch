package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room

import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.spreadsheet.Spreadsheet
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.spreadsheet.SpreadsheetState
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.spreadsheet.SpreadsheetDao
import io.reactivex.Observable
import javax.inject.Inject

class RoomDataManager
@Inject constructor(private val spreadsheetDao: SpreadsheetDao) {

    fun getSpreadsheetIdForEmail(email: String) =
            Observable.fromCallable { spreadsheetDao.findByEmail(email)?.spreadsheetId ?: "" }!!

    fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String) =

            Observable.fromCallable {

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

                spreadsheetId

            }!!
}