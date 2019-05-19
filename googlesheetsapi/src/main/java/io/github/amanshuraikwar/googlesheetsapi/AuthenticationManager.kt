package io.github.amanshuraikwar.googlesheetsapi

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * @author Amanshu Raikwar
 */
class AuthenticationManager
constructor(private val context: Context) {

    fun getLastSignedAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    companion object {
        const val CODE_SIGN_IN = 9001
    }

}