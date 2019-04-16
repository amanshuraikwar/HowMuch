package io.github.amanshuraikwar.howmuch.ui.stats

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.activity.HistoryActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.wallet.WalletActivity
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_stats.*
import javax.inject.Inject


class StatsFragment
@Inject constructor(): BaseFragment<StatsContract.View, StatsContract.Presenter>(), StatsContract.View {

    companion object {
        private const val REQ_CODE_WALLET = 10070
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
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
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
        pb.visibility = VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    override fun startWalletActivity(wallet: Wallet) {
        val intent = Intent(activity, WalletActivity::class.java)
        intent.putExtra(WalletActivity.KEY_WALLET, wallet)
        startActivityForResult(intent, REQ_CODE_WALLET)
    }

    override fun startTransactionActivity(transaction: Transaction) {
        val intent = Intent(activity, ExpenseActivity::class.java)
        intent.putExtra(ExpenseActivity.KEY_TRANSACTION, transaction)
        startActivityForResult(intent, REQ_CODE_TRANSACTION)
    }

    override fun startHistoryActivity() {
        startActivity(Intent(activity, HistoryActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_WALLET) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onWalletEdited()
            }
        }
    }

    @Module
    abstract class StatsModule {
        @Binds
        abstract fun presenter(presenter: StatsContract.StatsPresenter): StatsContract.Presenter
    }
}