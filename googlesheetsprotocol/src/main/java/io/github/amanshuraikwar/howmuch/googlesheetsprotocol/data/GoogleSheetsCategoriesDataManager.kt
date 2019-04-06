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
import io.github.amanshuraikwar.howmuch.protocol.CategoriesDataManager
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class GoogleSheetsCategoriesDataManager
@Inject constructor(private val authenticationManager: AuthenticationManager,
                    private val sheetsHelper: SheetsHelper,
                    @ApplicationContext private val context: Context,
                    private val localDataManager: LocalDataManager,
                    private val userDataManager: UserDataManager) : CategoriesDataManager {

    private fun GoogleSignInAccount.credential(): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(
                        context,
                        Arrays.asList(SheetsScopes.SPREADSHEETS)
                )
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(this.account)
    }

    override fun getAllCategories(): Observable<Iterable<Category>> {
        return userDataManager
                .getSignedInUser()
                .flatMap {
                    localDataManager.getSpreadsheetIdForEmail(it.email)
                }
                .flatMap {
                    sheetsHelper
                            .fetchCategories(
                                    spreadsheetId = it,
                                    googleAccountCredential =
                                    authenticationManager.getLastSignedAccount()!!.credential()
                            )
                }
    }

    override fun getCategoryById(id: String): Observable<Category> {
        // todo
        return Observable.fromCallable { Category("", "", TransactionType.DEBIT) }
    }

    override fun addCategory(category: Category): Observable<Category> {
        // todo
        return Observable.fromCallable { Category("", "", TransactionType.DEBIT) }
    }

    override fun updateCategory(category: Category): Observable<Category> {
        // todo
        return Observable.fromCallable { Category("", "", TransactionType.DEBIT) }
    }

    override fun deleteCategory(category: Category): Observable<Category> {
        // todo
        return Observable.fromCallable { Category("", "", TransactionType.DEBIT) }
    }
}