package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import javax.inject.Inject

class LocalDataManagerImpl
@Inject constructor(private val prefsDataManager: PrefsDataManager,
                    private val sqliteDataManager: SqliteDataManager) : LocalDataManager {

    override fun isInitialOnboardingDone() =
            prefsDataManager.isInitialOnboardingDone()

    override fun isSignedIn() =
            prefsDataManager.isSignedIn()

    override fun isSpreadsheetReady() =
            prefsDataManager.isSpreadsheetReady()

    override fun setInitialOnboardingDone(value: Boolean) =
            prefsDataManager.setInitialOnboardingDone(value)

    override fun setSignedIn(value: Boolean) =
            prefsDataManager.setSignedIn(value)

    override fun setSpreadsheetReady(value: Boolean) =
            prefsDataManager.setSpreadsheetReady(value)

    override fun getSpreadsheetIdForYearAndMonth(year: Int, month: Int) =
            sqliteDataManager.getSpreadsheetIdForYearAndMonth(year, month)

    override fun addSpreadsheetIdForYearAndMonth(spreadsheetId: String, year: Int, month: Int) =
            sqliteDataManager.addSpreadsheetIdForYearAndMonth(spreadsheetId, year, month)

    override fun isSpreadsheetReady(year: Int, month: Int) =
            sqliteDataManager.isSpreadsheetReady(year, month)

    override fun setSpreadsheetReady(year: Int, month: Int) =
            sqliteDataManager.setSpreadsheetReady(year, month)
}