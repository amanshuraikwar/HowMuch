package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.accounts.Account
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.layout_onboarding_create_sheet.*
import java.util.*
import javax.inject.Inject

class CreateSheetFragment @Inject constructor()
    : BaseFragment<CreateSheetContract.View, CreateSheetContract.Presenter>(), CreateSheetContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_onboarding_create_sheet, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        actionBtn.setOnClickListener {
            presenter.onCreateSheetClicked()
        }
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun updateLoading(message: String) {
        loadingTv.text = message
    }

    override fun showLoading() {
        loadingLl.visibility = VISIBLE
    }

    override fun hideLoading() {
        loadingLl.visibility = GONE
    }

    override fun showProceedButton() {
        negActionBtn.visibility = VISIBLE
    }

    override fun hideProceedButton() {
        negActionBtn.visibility = GONE
    }

    override fun showCreateSheetButton() {
        actionBtn.visibility = VISIBLE
    }

    override fun hideCreateSheetButton() {
        actionBtn.visibility = GONE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(parentRl, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showId(id: String) {
        spreadSheetIdTv.visibility = VISIBLE
        spreadSheetIdTv.text = id
    }

    override fun hideId() {
        spreadSheetIdTv.visibility = INVISIBLE
    }

    override fun showName(name: String) {
        spreadSheetNameTv.visibility = VISIBLE
        spreadSheetNameTv.text = name
    }

    override fun hideName() {
        spreadSheetNameTv.visibility = INVISIBLE
    }
}