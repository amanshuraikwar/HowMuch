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
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionDataManager
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class GoogleSheetsTransactionDataManager
@Inject constructor(private val authenticationManager: AuthenticationManager,
                    private val sheetsHelper: SheetsHelper,
                    @ApplicationContext private val context: Context,
                    private val localDataManager: LocalDataManager,
                    private val userDataManager: UserDataManager) : TransactionDataManager {

    private fun GoogleSignInAccount.credential(): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(
                        context,
                        Arrays.asList(SheetsScopes.SPREADSHEETS)
                )
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(this.account)
    }

    override fun getAllTransactions(): Observable<Iterable<Transaction>> {

        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper.fetchTransactions(
                            spreadsheetId = it,
                            googleAccountCredential =
                            authenticationManager.getLastSignedAccount()!!.credential()
                    )
                }
    }

    override fun getTransactionById(id: String): Observable<Transaction> {
        return getAllTransactions().map{ it.filter { txn -> txn.id == id }[0] }
    }

    override fun addTransaction(transaction: Transaction): Observable<Transaction> {

        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .addTransaction(
                                    transaction = transaction,
                                    spreadsheetId = it,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            // todo update id of txn
                            .toSingleDefault(transaction)
                            .toObservable()
                }
    }

    override fun updateTransaction(transaction: Transaction): Observable<Transaction> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .updateTransaction(
                                    transaction = transaction,
                                    spreadsheetId = it,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            .toSingleDefault(transaction)
                            .toObservable()
                }
    }

    override fun deleteTransaction(transaction: Transaction): Observable<Transaction> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .deleteTransaction(
                                    transaction = transaction,
                                    spreadsheetId = it,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                            .toSingleDefault(transaction)
                            .toObservable()
                }
    }
}