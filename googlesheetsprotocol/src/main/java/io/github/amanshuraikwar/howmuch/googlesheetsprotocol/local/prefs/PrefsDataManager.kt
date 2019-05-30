package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject

@SuppressLint("ApplySharedPref")
class PrefsDataManager
@Inject constructor(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val KEY_MONTHLY_EXPENSE_LIMIT = "monthly_expense_limit"
    }

    fun getMonthlyExpenseLimit(): Double {
        return sharedPrefs.getFloat(KEY_MONTHLY_EXPENSE_LIMIT, 10000.0F).toDouble()
    }

    fun setMonthlyExpenseLimit(limit: Double) {
        sharedPrefs.edit().putFloat(KEY_MONTHLY_EXPENSE_LIMIT, limit.toFloat()).commit()
    }
}