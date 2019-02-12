package io.github.amanshuraikwar.howmuch.data.local.prefs

import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Anything to do with accessing data over shared preferences.
 *
 * @author Amanshu Raikwar
 */
interface PrefsDataManager {

    // initial onboarding
    fun isInitialOnboardingDone(): Observable<Boolean>
    fun setInitialOnboardingDone(value: Boolean): Completable

    // current currency
    fun getCurrency(): String
    fun setCurrency(currency: String): Completable

    // transaction categories
    fun getCategories(): Observable<Set<String>>
    fun setCategories(categories: Set<String>): Completable
}