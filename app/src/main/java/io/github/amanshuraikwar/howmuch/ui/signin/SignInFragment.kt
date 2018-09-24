package io.github.amanshuraikwar.howmuch.ui.signin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.layout_onboarding_signin.*
import javax.inject.Inject

class SignInFragment @Inject constructor()
    : BaseFragment<SignInContract.View, SignInContract.Presenter>(), SignInContract.View {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_onboarding_signin, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        actionBtn.setOnClickListener {
            presenter.onSignInBtnClicked()
        }

        negActionBtn.setOnClickListener {
            presenter.onNegBtnClicked()
        }

        emailBtn.setOnClickListener {
            presenter.onEmailBtnClicked()
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
        actionBtn.visibility = GONE
    }

    override fun showSignInBtn() {
        actionBtn.visibility = VISIBLE
    }

    override fun hideNegBtn() {
        negActionBtn.visibility = GONE
    }

    override fun showNegBtn() {
        negActionBtn.visibility = VISIBLE
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentRl, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showGoogleUserInfo(photoUrl: String, email: String) {
//        profileCv.visibility = VISIBLE
//        emailBtn.visibility = VISIBLE
        emailBtn.isEnabled = true
        profileCv.isEnabled = true
        Glide.with(activity!!).load(photoUrl).into(profileIv)
        emailBtn.text = email
    }

    override fun hideGoogleUserInfo() {
//        profileCv.visibility = GONE
//        emailBtn.visibility = INVISIBLE
        emailBtn.isEnabled = false
        profileCv.isEnabled = false
    }
}