package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class LocalDataManagerImpl
@Inject constructor(private val prefsDataManager: PrefsDataManager,
                    private val sqliteDataManager: SqliteDataManager) : LocalDataManager {

    override fun isInitialOnboardingDone() =
            prefsDataManager.isInitialOnboardingDone()

    override fun isSignedIn() =
            prefsDataManager.isSignedIn()

    override fun setInitialOnboardingDone(value: Boolean) =
            prefsDataManager.setInitialOnboardingDone(value)

    override fun setSignedIn(value: Boolean) =
            prefsDataManager.setSignedIn(value)

    override fun getSpreadsheetIdForYearAndMonthAndEmail(year: Int, month: Int, email: String) =
            sqliteDataManager.getSpreadsheetIdForYearAndMonthAndEmail(year, month, email)

    override fun addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId: String, year: Int, month: Int, email: String) =
            sqliteDataManager.addSpreadsheetIdForYearAndMonthAndEmail(spreadsheetId, year, month, email)

    override fun isSpreadsheetReady(year: Int, month: Int, email: String) =
            sqliteDataManager.isSpreadsheetReady(year, month, email)

    override fun setSpreadsheetReady(year: Int, month: Int, email: String) =
            sqliteDataManager.setSpreadsheetReady(year, month, email)

    override fun getCurrency() = prefsDataManager.getCurrency()

    override fun setCurrency(currency: String) {
        prefsDataManager.setCurrency(currency)
    }

    override fun getCategories() = prefsDataManager.getCategories()

    override fun setCategories(categories: Set<String>) =
            prefsDataManager.setCategories(categories)
}