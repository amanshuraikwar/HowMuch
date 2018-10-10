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

    private var account: Account? = null
    private lateinit var email: String
    private var googleSignInAccount: GoogleSignInAccount? = null

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            account = getAccountAct()
            email = getEmailAct()
            googleSignInAccount = getGoogleSignInAccountAct()

            if (account == null || email == "" || googleSignInAccount == null) {
                onInvalidState(account, email)
            }
        }
    }

    protected fun getAccount() = account
    protected fun getEmail() = email
    protected fun getGoogleSignInAccount() = googleSignInAccount

    /**
     * Called during invalid state related to account or email.
     * i.e. When either of them is null.
     * @param account Account.
     * @param email Email
     */
    open fun onInvalidState(account: Account?, email: String?) {
        // do nothing
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getAccountAct(): Account? {

        if (authMan.hasPermissions()) {

            val account = authMan.getLastSignedAccount()?.account

            if (account == null) {
                return null
            } else {
                return account
            }
        } else {
            return null
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getGoogleSignInAccountAct(): GoogleSignInAccount? {

        if (authMan.hasPermissions()) {

            val account = authMan.getLastSignedAccount()

            if (account == null) {
                return null
            } else {
                return account
            }
        } else {
            return null
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun getEmailAct(): String {

        if (authMan.hasPermissions()) {

            val email = authMan.getLastSignedAccount()?.email

            if (email == null) {
                return ""
            } else {
                return email
            }
        } else {
            return ""
        }
    }
}