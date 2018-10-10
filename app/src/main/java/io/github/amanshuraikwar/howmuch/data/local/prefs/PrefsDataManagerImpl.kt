package io.github.amanshuraikwar.howmuch.data.local.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class PrefsDataManagerImpl
@Inject constructor(private val sharedPrefs: SharedPreferences): PrefsDataManager {

    companion object {
        private const val KEY_INITIAL_ONBOARDING_DONE = "initial_onboarding_done"
        private const val KEY_SIGNED_IN = "signed_in"
        private const val KEY_CURRENCY_SYMBOL = "currency_symbol"
    }

    override fun isInitialOnboardingDone(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPrefs.getBoolean(KEY_INITIAL_ONBOARDING_DONE, false))
            it.onComplete()
        }
    }

    override fun isSignedIn(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPrefs.getBoolean(KEY_SIGNED_IN, false))
            it.onComplete()
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun setInitialOnboardingDone(value: Boolean): Completable {
        return Completable.create{
            with (sharedPrefs.edit()) {
                putBoolean(KEY_INITIAL_ONBOARDING_DONE, value)
                commit()
            }
            it.onComplete()
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun setSignedIn(value: Boolean): Completable {
        return Completable.create{
            with (sharedPrefs.edit()) {
                putBoolean(KEY_SIGNED_IN, value)
                commit()
            }
            it.onComplete()
        }
    }

    override fun getCurrency(): String {
        return sharedPrefs.getString(KEY_CURRENCY_SYMBOL, "₹")
    }

    override fun setCurrency(currency: String) {
        with (sharedPrefs.edit()) {
            putString(KEY_CURRENCY_SYMBOL, currency)
            apply()
        }
    }
}