package io.github.amanshuraikwar.howmuch.ui.settings

import android.os.Bundle
import io.github.amanshuraikwar.howmuch.R
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expense_day.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        backIb.setOnClickListener({ finishAfterTransition() })
    }

    class Prefs1Fragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences_1)
        }
    }
}