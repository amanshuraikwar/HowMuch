package io.github.amanshuraikwar.howmuch.data.local.prefs

import io.reactivex.Completable
import io.reactivex.Observable

interface PrefsDataManager {
    fun isInitialOnboardingDone(): Observable<Boolean>
    fun isSignedIn(): Observable<Boolean>

    @Deprecated(message = "State is now kept per spreadsheet. Use sqlite data manager's method instead.")
    fun isSpreadsheetReady(): Observable<Boolean>

    fun setInitialOnboardingDone(value: Boolean): Completable
    fun setSignedIn(value: Boolean): Completable

    @Deprecated(message = "State is now kept per spreadsheet. Use sqlite data manager's method instead.")
    fun setSpreadsheetReady(value: Boolean): Completable
}