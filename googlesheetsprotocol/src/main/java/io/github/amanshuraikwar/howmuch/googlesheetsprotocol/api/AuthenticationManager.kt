package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext
import javax.inject.Inject

/**
 * @author Amanshu Raikwar
 */

class AuthenticationManager
@Inject constructor(@ApplicationContext private val context: Context) {

    fun getLastSignedAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    companion object {
        const val CODE_SIGN_IN = 9001
    }

}