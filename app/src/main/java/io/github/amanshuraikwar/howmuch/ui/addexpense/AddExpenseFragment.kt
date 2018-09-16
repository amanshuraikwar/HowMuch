package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.accounts.Account
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_expense.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddExpenseFragment @Inject constructor()
    : BaseFragment<AddExpenseContract.View, AddExpenseContract.Presenter>(), AddExpenseContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_expense, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initLoading()
    }

    private fun init() {
        submitBtn.setOnClickListener {
            presenter.onSubmitClicked(
                    Expense("",
                            dateEt.text.toString(),
                            amountEt.text.toString(),
                            descriptionEt.text.toString(),
                            categorySp.selectedItem.toString()))
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        dateEt.setText(dateFormat.format(Date()).toString())
    }

    private fun initLoading() {
        loadingRetryBtn.setOnClickListener {
            presenter.onLoadingRetryClicked()
        }
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingOverlay() {
        loadingParentLl.visibility = VISIBLE
        loadingPb.visibility = VISIBLE
        loadingTv.visibility = VISIBLE
        loadingRetryBtn.visibility = GONE
    }

    override fun hideLoadingOverlay() {
        loadingParentLl.visibility = GONE
    }

    override fun populateCategories(categories: List<String>) {
        categorySp.adapter = ArrayAdapter(activity, R.layout.textview_spinner, categories)
    }

    override fun showErrorOverlay() {
        loadingParentLl.visibility = VISIBLE
        loadingPb.visibility = GONE
        loadingTv.visibility = GONE
        loadingRetryBtn.visibility = VISIBLE
    }

    override fun setSubmitBtnText(text: String) {
        submitBtn.text = text
    }

    override fun enableSubmitBtn() {
        submitBtn.isEnabled = true
    }

    override fun disableSubmitBtn() {
        submitBtn.isEnabled = false
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(parentFl, message, Snackbar.LENGTH_SHORT).show()
    }
}