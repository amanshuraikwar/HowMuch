package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.room.RoomDataManager
import javax.inject.Inject

class LocalDataManagerImpl
@Inject constructor(private val prefsDataManager: PrefsDataManager,
                    private val roomDataManager: RoomDataManager) : LocalDataManager {

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
            roomDataManager.getSpreadsheetIdForYearAndMonth(year, month)

    override fun addSpreadsheetIdForYearAndMonth(spreadsheetId: String, year: Int, month: Int) =
            roomDataManager.addSpreadsheetIdForYearAndMonth(spreadsheetId, year, month)

}