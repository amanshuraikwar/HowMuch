package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import javax.inject.Inject

class LocalDataManagerImpl
@Inject constructor(private val prefsDataManager: PrefsDataManager,
                    private val sqliteDataManager: SqliteDataManager) : LocalDataManager {

    override fun isInitialOnboardingDone() =
            prefsDataManager.isInitialOnboardingDone()

    override fun setInitialOnboardingDone(value: Boolean) =
            prefsDataManager.setInitialOnboardingDone(value)

    override fun getCurrency()=
            prefsDataManager.getCurrency()

    override fun setCurrency(currency: String) =
            prefsDataManager.setCurrency(currency)

    override fun getCategories() =
            prefsDataManager.getCategories()

    override fun setCategories(categories: Set<String>) =
            prefsDataManager.setCategories(categories)

    override fun getSpreadsheetIdForEmail(email: String) =
            sqliteDataManager.getSpreadsheetIdForEmail(email)

    override fun addSpreadsheetIdForEmail(spreadsheetId: String, email: String) =
            sqliteDataManager.addSpreadsheetIdForEmail(spreadsheetId, email)

    override fun isSpreadsheetReady(email: String) =
            sqliteDataManager.isSpreadsheetReady(email)

    override fun setSpreadsheetReady(email: String) =
            sqliteDataManager.setSpreadsheetReady(email)
}