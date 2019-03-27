package io.github.amanshuraikwar.howmuch.ui.history

import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*
import javax.inject.Inject


class HistoryFragment
@Inject constructor(): BaseFragment<HistoryContract.View, HistoryContract.Presenter>(), HistoryContract.View {

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    private var adapter: MultiItemListAdapter<*>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onTransactionEdited()
            }
        }
    }

    private fun init() {

        itemsRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        if (adapter == null) {
            adapter = MultiItemListAdapter(activity, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemsRv.setOnScrollChangeListener {
                _, _, _, _, _ ->
            }
        }

        toolbar.inflateMenu(R.menu.refresh_navigation)
        toolbar.setOnMenuItemClickListener {
            presenter.onRefreshClicked()
            return@setOnMenuItemClickListener true
        }

    }

    override fun getGoogleAccountCredential(googleAccount: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(googleAccount)
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun startTransactionActivity(transaction: Transaction) {
        val intent = Intent(activity, ExpenseActivity::class.java)
        intent.putExtra(ExpenseActivity.KEY_TRANSACTION, transaction)
        startActivityForResult(intent, REQ_CODE_TRANSACTION)
    }

    override fun setSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_sync_problem_red_24dp)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_autorenew_black_24dp)
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
    }

    override fun showLoading(message: String) {
        pb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = View.GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    @Module
    abstract class HistoryModule {
        @Binds
        abstract fun presenter(presenter: HistoryContract.HistoryPresenter): HistoryContract.Presenter
    }
}