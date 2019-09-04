package io.github.amanshuraikwar.howmuch.protocol

import io.reactivex.Completable
import io.reactivex.Observable

interface UserDataManager {

    fun signIn(user: User): Observable<User>

    fun signOut(): Completable

    fun isSignedIn(): Observable<Boolean>

    fun getSignedInUser(): Observable<User>

    @Deprecated("Use sum of all categories instead")
    fun getMonthlyExpenseLimit(): Observable<Double>

    fun setMonthlyExpenseLimit(limit: Double): Completable

    fun getSpreadSheetId(): Observable<String>
}