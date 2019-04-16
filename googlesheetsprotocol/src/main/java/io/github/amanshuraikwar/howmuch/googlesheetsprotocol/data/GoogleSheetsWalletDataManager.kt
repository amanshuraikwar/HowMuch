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
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.protocol.WalletDataManager
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class GoogleSheetsWalletDataManager
@Inject constructor(private val authenticationManager: AuthenticationManager,
                    private val sheetsHelper: SheetsHelper,
                    @ApplicationContext private val context: Context,
                    private val localDataManager: LocalDataManager,
                    private val userDataManager: UserDataManager)
    : WalletDataManager {

    private fun GoogleSignInAccount.credential(): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(
                        context,
                        Arrays.asList(SheetsScopes.SPREADSHEETS)
                )
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(this.account)
    }

    override fun getAllWallets(): Observable<Iterable<Wallet>> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper.fetchWallets(
                            spreadsheetId = it,
                            googleAccountCredential =
                            authenticationManager.getLastSignedAccount()!!.credential()
                    )
                }
    }

    override fun getWalletById(id: String): Observable<Wallet> {
        // todo
        return Observable.fromCallable { Wallet("", "", 0.0, false) }
    }

    override fun addWallet(wallet: Wallet): Observable<Wallet> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .addWallet(
                                    wallet = wallet,
                                    spreadsheetId = it,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            // todo update id of wallet
                            .toSingleDefault(wallet)
                            .toObservable()
                }
    }

    override fun updateWallet(wallet: Wallet): Observable<Wallet> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .updateWallet(
                                    spreadsheetId = it,
                                    wallet = wallet,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            .toSingleDefault(wallet)
                            .toObservable()
                }
    }

    override fun deleteWallet(wallet: Wallet): Observable<Wallet> {
        wallet.active = false
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .updateWallet(
                                    spreadsheetId = it,
                                    wallet = wallet,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            .toSingleDefault(wallet)
                            .toObservable()
                }
    }
}