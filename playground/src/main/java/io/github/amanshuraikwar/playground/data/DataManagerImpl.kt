package io.github.amanshuraikwar.playground.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.AuthenticationManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.SheetsDataSource
import io.github.amanshuraikwar.howmuch.protocol.User
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DataManagerImpl
@Inject constructor(private val userDataManager: UserDataManager,
                    private val sheetsDataSource: SheetsDataSource,
                    private val authenticationManager: AuthenticationManager,
                    @ApplicationContext private val context: Context) : DataManager {

    private fun GoogleSignInAccount.credential(): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(
                        context,
                        Arrays.asList(SheetsScopes.SPREADSHEETS)
                )
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(this.account)
    }

    private var cachedSongs: List<Song>? = null

    override fun getAllSongs(): Observable<List<Song>> {
        // todo
        return if (cachedSongs == null )
            sheetsDataSource
                .readSpreadSheet(
                        "1q-f9qOPPo6LUMx7mp9Ly3o26jx7tOBATz5DEEB_Gob8",
                        "Sheet1!A1:K",
                        authenticationManager.getLastSignedAccount()!!.credential()
                )
                .map {

                    val sdf = SimpleDateFormat("MMMM dd, yyyy HH:mma", Locale.UK)

                    cachedSongs = it.map {
                        rowData ->

                        if (rowData.size == 11) {

                            Song(
                                    sdf.parse(
                                            {
                                                val parts = rowData[0].toString().split(" at ")
                                                parts[0] + " " + parts[1]
                                            }.invoke()
                                    ),
                                    rowData[1].toString(),
                                    rowData[2].toString(),
                                    rowData[3].toString(),
                                    rowData[10].toString(),
                                    rowData[5].toString()
                            )

                        } else {

                            Song(
                                    sdf.parse(
                                            {
                                                val parts = rowData[0].toString().split(" at ")
                                                parts[0] + " " + parts[1]
                                            }.invoke()
                                    ),
                                    rowData[1].toString(),
                                    rowData[2].toString(),
                                    rowData[3].toString(),
                                    "",
                                    rowData[5].toString()
                            )

                        }

                    }

                    cachedSongs
                }
        else
            Observable.fromCallable { cachedSongs }
    }

    override fun signIn(user: User)
            = userDataManager.signIn(user)

    override fun signOut()
            = userDataManager.signOut()

    override fun isSignedIn(): Observable<Boolean> {
        return Observable
                .fromCallable {
                    authenticationManager.getLastSignedAccount() != null
                }
    }

    override fun getSignedInUser()
            = userDataManager.getSignedInUser()

    override fun getMonthlyExpenseLimit()
            = userDataManager.getMonthlyExpenseLimit()

    override fun setMonthlyExpenseLimit(limit: Double)
            = userDataManager.setMonthlyExpenseLimit(limit)
}