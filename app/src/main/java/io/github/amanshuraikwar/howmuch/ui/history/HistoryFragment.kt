package io.github.amanshuraikwar.howmuch.ui.history

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.search.SearchActivity
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject


class HistoryFragment
@Inject constructor(): BaseFragment<HistoryContract.View, HistoryContract.Presenter>(), HistoryContract.View {

    companion object {

        private const val REQ_CODE_TRANSACTION = 10069

        // Filters for the transaction list to be shown
        // Format: transaction_type=DEBIT|CREDIT|ALL&category_id=id1|id2|id3&wallet_id=id1|id2|id3
        const val KEY_FILTERS = "filters"
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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_active_24dp)
        toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        toolbar.inflateMenu(R.menu.search_refresh_navigation)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.search -> {
                    val searchMenuView = toolbar.findViewById(R.id.search) as View
                    val options = ActivityOptions.makeSceneTransitionAnimation(activity, searchMenuView, getString(R.string.transition_search_back)).toBundle()
                    startActivity(Intent(activity, SearchActivity::class.java), options)
                }
                R.id.refresh -> {
                    presenter.onRefreshClicked()
                }
            }

            return@setOnMenuItemClickListener true
        }

        allTimeCtv.setOnClickListener {
            if (!allTimeCtv.isChecked) {
                allTimeCtv.isChecked = true
                thisMonthCtv.isChecked = false
                thisWeekCtv.isChecked = false
                presenter.onAllTimeChecked()
            }
        }

        thisMonthCtv.setOnClickListener {
            if (!thisMonthCtv.isChecked) {
                allTimeCtv.isChecked = false
                thisMonthCtv.isChecked = true
                thisWeekCtv.isChecked = false
                presenter.onThisMonthChecked()
            }
        }

        thisWeekCtv.setOnClickListener {
            if (!thisWeekCtv.isChecked) {
                allTimeCtv.isChecked = false
                thisMonthCtv.isChecked = false
                thisWeekCtv.isChecked = true
                presenter.onThisWeekChecked()
            }
        }
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
        toolbar.menu.getItem(1).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_sync_problem_red_24dp)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(1).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_autorenew_black_24dp)
    }

    override fun getFilters(): String? {
        return arguments?.getString(KEY_FILTERS)
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