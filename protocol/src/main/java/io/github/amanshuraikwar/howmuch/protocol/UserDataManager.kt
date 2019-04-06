package io.github.amanshuraikwar.howmuch.protocol

import io.reactivex.Completable
import io.reactivex.Observable

interface UserDataManager {

    fun signIn(user: User): Observable<User>

    fun signOut(): Completable

    fun isSignedIn(): Observable<Boolean>

    fun getSignedInUser(): Observable<User>

}