package io.github.amanshuraikwar.howmuch.ui.signin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment @Inject constructor()
    : BaseFragment<SignInContract.View, SignInContract.Presenter>(), SignInContract.View {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        signInBtn.setOnClickListener {
            presenter.onSignInBtnClicked()
        }

        signOutBtn.setOnClickListener {
            presenter.onSignOutBtnClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AuthenticationManager.CODE_SIGN_IN) {
            presenter.onSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data).isSuccessful)
        }
    }

    override fun initiateSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, AuthenticationManager.CODE_SIGN_IN)
    }

    override fun initiateSignOut() {
        googleSignInClient.signOut()
    }

    override fun hideSignInBtn() {
        signInBtn.visibility = GONE
    }

    override fun showSignInBtn() {
        signInBtn.visibility = VISIBLE
    }

    override fun hideSignOutBtn() {
        signOutBtn.visibility = GONE
    }

    override fun showSignOutBtn() {
        signOutBtn.visibility = VISIBLE
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentLl, message, Snackbar.LENGTH_SHORT).show()
    }
}