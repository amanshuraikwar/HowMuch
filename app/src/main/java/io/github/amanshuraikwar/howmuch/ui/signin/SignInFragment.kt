package io.github.amanshuraikwar.howmuch.ui.signin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
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
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject


class SignInFragment @Inject constructor()
    : BaseFragment<SignInContract.View, SignInContract.Presenter>(), SignInContract.View, OnboardingScreen {

    private val TAG = Util.getTag(this)

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
        createSheetBtn.setOnClickListener {
            presenter.onSignInBtnClicked()
        }

        proceedBtn.setOnClickListener {
            presenter.onProceedBtnClicked()
        }

        emailBtn.setOnClickListener {
            presenter.onEmailBtnClicked()
        }

        profileCv.isEnabled = false
        emailBtn.isEnabled = false

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: called")

        if (requestCode == AuthenticationManager.CODE_SIGN_IN) {
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
        createSheetBtn.visibility = GONE
    }

    override fun showSignInBtn() {
        createSheetBtn.visibility = VISIBLE
    }

    override fun hideProceedBtn() {
        proceedBtn.visibility = GONE
    }

    override fun showProceedBtn() {
        proceedBtn.visibility = VISIBLE
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentRl, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showGoogleUserInfo(photoUrl: String, email: String) {
        profileCv.visibility = VISIBLE
        emailBtn.visibility = VISIBLE
        emailBtn.isEnabled = true
        profileCv.isEnabled = true
        Glide.with(activity!!).load(photoUrl).into(profileIv)
        emailBtn.text = email
    }

    override fun hideGoogleUserInfo() {
        profileCv.visibility = INVISIBLE
        emailBtn.visibility = INVISIBLE
        emailBtn.isEnabled = false
        profileCv.isEnabled = false
    }

    override fun selected() {
        presenter.onScreenSelected()
    }
}