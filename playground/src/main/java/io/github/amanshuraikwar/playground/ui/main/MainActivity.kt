package io.github.amanshuraikwar.playground.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.AuthenticationManager
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import io.github.amanshuraikwar.playground.R
import io.github.amanshuraikwar.playground.data.Song
import io.github.amanshuraikwar.playground.ui.ListItemTypeFactory
import io.github.amanshuraikwar.playground.ui.widget.SettingsBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    companion object {
        private const val LIST_STATE_KEY = "state"
    }

    private val logTag = Util.getTag(this)

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private var adapter: MultiItemListAdapter<*>? = null

    private var mListState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save list state
        mListState = itemsRv.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, mListState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY)
        }
    }

    override fun onResume() {
        super.onResume()

        if (mListState != null) {
            itemsRv.layoutManager?.onRestoreInstanceState(mListState)
        }
    }

    private fun init() {

        profilePicCv.setOnClickListener { presenter.onUserIconClicked() }

        itemsRv.layoutManager = LinearLayoutManager(this)

        if (adapter == null) {
            adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        actionBtn.setOnClickListener { presenter.onActionBtnClicked() }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
                && newConfig.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_NORMAL)) {
            songDetailCl.visibility = VISIBLE
        } else {
            songDetailCl.visibility = GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        Log.d(logTag, "onActivityResult: called")

        if (requestCode == AuthenticationManager.CODE_SIGN_IN) {
            presenter.onSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data).isSuccessful)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentCl, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        iconIv.visibility = VISIBLE
        msgTv.visibility = VISIBLE
        msgTv.text = message
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

    override fun showGoogleUserInfo(name: String, photoUrl: String, email: String) {
        Glide.with(this).load(photoUrl).into(profilePicIv)
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    @Suppress("MoveLambdaOutsideParentheses")
    override fun showSettings(name: String, photoUrl: String, email: String) {

        SettingsBottomSheetDialogFragment()
                .init(
                        name,
                        photoUrl,
                        email,
                        {
                            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogTheme)
                                    .setTitle("Are you sure you want to sign out?")
                                    .setPositiveButton(
                                            "Sign Out",
                                            {
                                                _, _ ->
                                                presenter.onSignOutClicked()
                                                it.dismiss()
                                            }
                                    )
                                    .setNegativeButton("Cancel", null)
                                    .show()
                        }
                )
                .show(supportFragmentManager, "settings-dialog")
    }

    override fun showSongDetails(song: Song) {
        Glide.with(this).load(song.artUrl).into(songArtIv)
        songNameTv.text = song.name
        songArtistTv.text = song.artist
        songAlbumTv.text = song.album
    }

    override fun showActionBtn(msg: String) {
        actionBtn.visibility = VISIBLE
        actionBtn.text = msg
    }

    override fun hideActionBtn() {
        actionBtn.visibility = GONE
    }

    override fun showLoading(message: String) {
        pb.visibility = VISIBLE
        iconIv.visibility = GONE
        msgTv.visibility = GONE
        actionBtn.visibility = GONE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: MainContract.PresenterImpl): MainContract.Presenter

        @Binds
        @ActivityContext
        abstract fun b(a: MainActivity): AppCompatActivity
    }

}
