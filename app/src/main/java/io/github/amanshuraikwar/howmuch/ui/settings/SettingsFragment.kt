package io.github.amanshuraikwar.howmuch.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


class SettingsFragment @Inject constructor()
    : BaseFragment<SettingsContract.View, SettingsContract.Presenter>(), SettingsContract.View {

    private val TAG = Util.getTag(this)

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbar.isSelected = sv.canScrollVertically(-1)
            }
        }

        currencyFl.setOnClickListener {
            presenter.onCurrencyClicked()
        }

        aboutFl.setOnClickListener {
            presenter.onAboutClicked()
        }

        logoutFl.setOnClickListener {
            presenter.onLogoutClicked()
        }

    }

    override fun initiateSignOut() {
        googleSignInClient.signOut()
    }

    override fun updateCurrency(currency: String) {
        currencySymbolTv.text = currency
    }

    override fun updateProfilePic(url: String) {
        Glide.with(activity!!).load(url).into(profilePicIv)
    }

    override fun updateName(name: String) {
        nameTv.text = name
    }

    override fun updateEmail(email: String) {
        emailTv.text = email
    }

    override fun showCurrencyDialog(currencyList: List<String>) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Choose currency")

        builder.setItems(currencyList.toTypedArray())
        {
            _, which ->
            presenter.onCurrencySelected(currencyList[which])
        }

        val dialog = builder.create()
        dialog.show()
    }
}