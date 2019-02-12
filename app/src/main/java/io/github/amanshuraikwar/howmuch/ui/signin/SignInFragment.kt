package io.github.amanshuraikwar.howmuch.ui.signin

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.util.*


class SignInFragment
    : BaseFragment<SignInContract.View, SignInContract.Presenter>(), SignInContract.View {

    private val logTag = Util.getTag(this)

    @SuppressLint("InflateParams")
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

        proceedBtn.setOnClickListener {
            presenter.onProceedBtnClicked()
        }

        editIb.setOnClickListener {
            presenter.onEditBtnClicked()
        }

        horizontalPb.max = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        Log.d(logTag, "onActivityResult: called")

        if (requestCode == AuthenticationManager.CODE_SIGN_IN) {
            presenter.onSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data).isSuccessful)
        }
    }

    override fun initiateSignIn() {
        startActivityForResult(
                activity.googleSignInClient.signInIntent,
                AuthenticationManager.CODE_SIGN_IN
        )
    }

    override fun initiateSignOut() {
        activity.googleSignInClient.signOut()
    }

    override fun showSignInInfo() {
        titleTv.visibility = VISIBLE
        descriptionTv.visibility = VISIBLE
        dummyV.visibility = VISIBLE
    }

    override fun hideSignInBtn() {
        signInBtn.visibility = GONE
    }

    override fun showSignInBtn() {
        signInBtn.visibility = VISIBLE
    }

    override fun hideProceedBtn() {
        proceedBtn.visibility = GONE
    }

    @SuppressLint("SetTextI18n")
    override fun showProceedBtn(name: String) {
        proceedBtn.text = "Continue as $name"
        proceedBtn.visibility = VISIBLE
    }

    override fun hideGoogleAccountEdit() {
        editIb.visibility = GONE
    }

    override fun showGoogleAccountEdit() {
        editIb.visibility = VISIBLE
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentCl, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showGoogleUserInfo(name: String, photoUrl: String, email: String) {

        profilePicCv.visibility = VISIBLE
        nameTv.visibility = VISIBLE
        emailTv.visibility = VISIBLE
        editIb.visibility = VISIBLE

        Glide.with(activity).load(photoUrl).into(profilePicIv)
        nameTv.text = name
        emailTv.text = email
    }

    override fun hideGoogleUserInfo() {

        profilePicCv.visibility = GONE
        nameTv.visibility = GONE
        emailTv.visibility = GONE
        editIb.visibility = GONE

        Glide.with(activity).clear(profilePicIv)
        nameTv.text  = ""
        emailTv.text  = ""
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun showProgress(progress: Int) {
        horizontalPb.progress = progress
    }

    override fun showLoading(message: String) {
        loadingPb.visibility = VISIBLE
        loadingPbScrim.visibility = VISIBLE
        horizontalPb.visibility = VISIBLE
    }

    override fun hideLoading() {
        loadingPb.visibility = GONE
        loadingPbScrim.visibility = GONE
        horizontalPb.visibility = GONE
    }

    override fun showError(message: String) {
        // do nothing
    }

    @Module
    abstract class SignInModule {
        @Binds
        abstract fun presenter(presenter: SignInContract.SignInPresenter): SignInContract.Presenter
    }
}