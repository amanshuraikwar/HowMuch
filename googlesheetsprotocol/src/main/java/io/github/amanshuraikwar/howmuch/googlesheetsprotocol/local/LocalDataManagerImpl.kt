package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local

import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.RoomDataManager
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class LocalDataManagerImpl
@Inject constructor(private val roomDataManager: RoomDataManager,
                    private val prefsDataManager: PrefsDataManager)
    : LocalDataManager {

    override fun getSpreadsheetIdForEmail(email: String): Observable<String> {
        return roomDataManager.getSpreadsheetIdForEmail(email)
    }

    override fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String): Observable<String> {
        return roomDataManager.addSpreadsheetIdForEmail(spreadsheetId, email)
    }

    override fun getMonthlyExpenseLimit(): Observable<Double> {
        return Observable.fromCallable {
            prefsDataManager.getMonthlyExpenseLimit()
        }
    }

    override fun setMonthlyExpenseLimit(limit: Double): Completable {
        return Observable.fromCallable {
            prefsDataManager.setMonthlyExpenseLimit(limit)
        }.ignoreElements()
    }
}