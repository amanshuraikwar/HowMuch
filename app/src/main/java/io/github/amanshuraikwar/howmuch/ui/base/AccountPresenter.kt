package io.github.amanshuraikwar.howmuch.ui.base

import android.accounts.Account
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager

open class AccountPresenter<View: BaseView>

constructor(appBus: AppBus,
            dataManager: DataManager,
            private val authMan: AuthenticationManager = dataManager.getAuthenticationManager())

    : BasePresenterImpl<View>(appBus, dataManager), BasePresenter<View> {

    protected fun getAccount(): Account? {
        return if (authMan.hasPermissions()) {
            authMan.getLastSignedAccount()?.account
        } else {
            null
        }
    }

    protected fun getEmail(): String? {
        return if (authMan.hasPermissions()) {
            authMan.getLastSignedAccount()?.email
        } else {
            null
        }
    }

    protected fun getDisplayName(): String? {
        return if (authMan.hasPermissions()) {
            authMan.getLastSignedAccount()?.displayName
        } else {
            null
        }
    }

    protected fun getPhotoUrl(): String? {
        return if (authMan.hasPermissions()) {
            authMan.getLastSignedAccount()?.photoUrl.toString()
        } else {
            null
        }
    }

    protected fun getGoogleSignInAccount(): GoogleSignInAccount? {
        return if (authMan.hasPermissions()) {
            authMan.getLastSignedAccount()
        } else {
            null
        }
    }

    protected fun isSignedIn()
            = authMan.hasPermissions() && (authMan.getLastSignedAccount() != null)
}