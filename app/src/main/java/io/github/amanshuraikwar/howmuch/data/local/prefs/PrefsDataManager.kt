package io.github.amanshuraikwar.howmuch.data.local.prefs

import io.reactivex.Completable
import io.reactivex.Observable

interface PrefsDataManager {
    fun isInitialOnboardingDone(): Observable<Boolean>
    fun isSignedIn(): Observable<Boolean>
    fun setInitialOnboardingDone(value: Boolean): Completable
    fun setSignedIn(value: Boolean): Completable
    fun getCurrency(): String
    fun setCurrency(currency: String)
}