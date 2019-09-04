package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.SheetsHelper
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.AuthenticationManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.protocol.User
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.reactivex.Completable
import io.reactivex.Observable
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class  GoogleSheetsUserDataManager
@Inject constructor(private val authenticationManager: AuthenticationManager,
                    private val sheetsHelper: SheetsHelper,
                    @ApplicationContext private val context: Context,
                    private val localDataManager: LocalDataManager)
    : UserDataManager {

    private fun GoogleSignInAccount.credential(): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(
                        context,
                        Arrays.asList(SheetsScopes.SPREADSHEETS)
                )
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(this.account)
    }

    private fun GoogleSignInAccount.user(): User {
        return User(this.id!!, this.displayName!!, this.email!!, this.photoUrl?.toString() ?: "")
    }

    override fun signIn(user: User): Observable<User> {

        return Observable
                .fromCallable {

                    if (authenticationManager.getLastSignedAccount() == null) {
                        throw Exception("User not signed in.")
                    } else {
                        authenticationManager.getLastSignedAccount()!!
                    }
                }
                .flatMap {

                    googleSignInAccount ->

                    localDataManager
                            .getSpreadsheetIdForEmail(googleSignInAccount.user().email)
                            .flatMap {

                                id ->

                                if (id == "") {

                                    sheetsHelper
                                            .init(googleSignInAccount.credential())
                                            .flatMap {
                                                newId ->
                                                localDataManager.addSpreadsheetIdForEmail(
                                                        newId,
                                                        googleSignInAccount.user().email
                                                )
                                            }
                                            .map { googleSignInAccount }

                                } else {
                                    Observable.fromCallable { googleSignInAccount }
                                }
                            }
                }
                .map { it.user() }
    }

    override fun signOut(): Completable {

        return Completable.fromCallable {

            if (authenticationManager.getLastSignedAccount() != null) {
                throw Exception("User not signed out.")
            }
        }
    }

    override fun isSignedIn(): Observable<Boolean> {

        return Observable
                .fromCallable {
                    authenticationManager.getLastSignedAccount() != null
                }.flatMap {

                    hasAccount ->

                    if (hasAccount) {

                        localDataManager
                                .getSpreadsheetIdForEmail(
                                        authenticationManager.getLastSignedAccount()!!.user().email
                                )
                                .map { it != "" }

                    } else {
                        Observable.fromCallable { false }
                    }
                }
    }

    override fun getSignedInUser(): Observable<User> {

        return Observable.fromCallable {

            if (authenticationManager.getLastSignedAccount() == null) {
                throw Exception("User not signed in.")
            } else {
                authenticationManager.getLastSignedAccount()!!.user()
            }
        }
    }

    override fun getMonthlyExpenseLimit(): Observable<Double> {
        return localDataManager.getMonthlyExpenseLimit()
    }

    override fun setMonthlyExpenseLimit(limit: Double): Completable {
        return localDataManager.setMonthlyExpenseLimit(limit)
    }

    override fun getSpreadSheetId(): Observable<String> {
        return getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
    }
}