package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject

@SuppressLint("ApplySharedPref")
class PrefsDataManager
@Inject constructor(private val sharedPrefs: SharedPreferences)