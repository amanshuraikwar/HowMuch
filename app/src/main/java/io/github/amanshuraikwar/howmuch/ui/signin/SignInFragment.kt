package io.github.amanshuraikwar.howmuch.ui.signin

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
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.base.util.Util;
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.AuthenticationManager
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject


class SignInFragment
    : BaseFragment<SignInContract.View, SignInContract.Presenter>(), SignInContract.View {

    private val logTag = Util.getTag(this)

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

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
                googleSignInClient.signInIntent,
                AuthenticationManager.CODE_SIGN_IN
        )
    }

    override fun initiateSignOut() {
        googleSignInClient.signOut()
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
    abstract class DiModule {

        @Binds
        abstract fun presenter(presenter: SignInContract.SignInPresenter): SignInContract.Presenter
    }

    @Module
    class DiProvides {

        @Provides
        @ActivityContext
        fun activity(a: SignInFragment): AppCompatActivity {
            return a.activity
        }
    }
}