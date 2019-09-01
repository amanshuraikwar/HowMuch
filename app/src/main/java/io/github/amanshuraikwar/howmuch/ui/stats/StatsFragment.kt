package io.github.amanshuraikwar.howmuch.ui.stats

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.monthlybudget.MonthlyBudgetActivity
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_stats.*
import javax.inject.Inject


class StatsFragment
@Inject constructor(): BaseFragment<StatsContract.View, StatsContract.Presenter>(), StatsContract.View {

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    private var adapter: MultiItemListAdapter<*>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

        itemsRv.layoutManager = LinearLayoutManager(activity)

        if (adapter == null) {
            adapter = MultiItemListAdapter(activity, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        toolbar.inflateMenu(R.menu.refresh_navigation)
        toolbar.setOnMenuItemClickListener {
            presenter.onRefreshClicked()
            return@setOnMenuItemClickListener true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemsRv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbar.isSelected = itemsRv.canScrollVertically(-1)
            }
        }
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun setSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.round_sync_problem_24)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.round_sync_24)
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {

    }

    override fun showLoading(message: String) {
        pb.visibility = VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    override fun startTransactionActivity(transaction: Transaction,
                                          category: Category) {
        startActivityForResult(
                {
                    val intent = Intent(activity, ExpenseActivity::class.java)
                    intent.putExtra(ExpenseActivity.KEY_TRANSACTION, transaction)
                    intent.putExtra(ExpenseActivity.KEY_CATEGORY, category)
                    intent
                }.invoke(),
                REQ_CODE_TRANSACTION
        )
    }

    override fun startHistoryActivity(filter: String?) {
        startActivity(Intent(activity, HistoryActivity::class.java))
    }

    override fun startMonthlyBudgetActivity() {
        startActivity(Intent(activity, MonthlyBudgetActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onTransactionEdited()
            }
        }
    }

    @Module
    abstract class StatsModule {
        @Binds
        abstract fun presenter(presenter: StatsContract.StatsPresenter): StatsContract.Presenter
    }
}