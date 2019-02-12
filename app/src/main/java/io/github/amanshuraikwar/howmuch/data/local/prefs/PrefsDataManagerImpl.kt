package io.github.amanshuraikwar.howmuch.data.local.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@SuppressLint("ApplySharedPref")
class PrefsDataManagerImpl
@Inject constructor(private val sharedPrefs: SharedPreferences)
    : PrefsDataManager {

    companion object {
        private const val KEY_INITIAL_ONBOARDING_DONE = "initial_onboarding_done"
        private const val KEY_CURRENCY_SYMBOL = "currency_symbol"
        private const val KEY_CATEGORIES = "categories"
    }

    override fun isInitialOnboardingDone() =
            Observable.just(sharedPrefs.getBoolean(KEY_INITIAL_ONBOARDING_DONE, false))!!

    override fun setInitialOnboardingDone(value: Boolean) =

            Completable.fromCallable {

                with (sharedPrefs.edit()) {
                    putBoolean(KEY_INITIAL_ONBOARDING_DONE, value)
                    commit()
                }

            }!!

    override fun getCurrency() = sharedPrefs.getString(KEY_CURRENCY_SYMBOL, "₹")!!

    override fun setCurrency(currency: String) =

            Completable.fromCallable {

                with (sharedPrefs.edit()) {
                    putString(KEY_CURRENCY_SYMBOL, currency)
                    commit()
                }

            }!!

    override fun getCategories(): Observable<Set<String>> =
            Observable.just(sharedPrefs.getStringSet(KEY_CATEGORIES, setOf()))

    override fun setCategories(categories: Set<String>) =

            Completable.fromCallable {

                with (sharedPrefs.edit()) {
                    putStringSet(KEY_CATEGORIES, categories)
                    commit()
                }

            }!!
}