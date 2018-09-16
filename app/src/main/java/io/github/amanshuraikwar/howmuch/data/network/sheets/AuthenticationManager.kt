package io.github.amanshuraikwar.howmuch.data.network.sheets

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import javax.inject.Inject

/**
 * @author Amanshu Raikwar
 */

class AuthenticationManager
@Inject constructor(@ApplicationContext private val context: Context) {

    fun hasPermissions(): Boolean {

        if (getLastSignedAccount() != null) {
            return GoogleSignIn.hasPermissions(getLastSignedAccount(), Scope(SheetsScopes.SPREADSHEETS))
        }

        return false
    }

    fun getLastSignedAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    companion object {
        const val CODE_SIGN_IN = 9001
    }

}