package io.github.amanshuraikwar.howmuch.ui.stats

import android.accounts.Account
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import java.util.*
import javax.inject.Inject

class StatsFragment @Inject constructor()
    : BaseFragment<StatsContract.View, StatsContract.Presenter>(), StatsContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        itemsRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        if (adapter == null) {
            adapter = MultiItemListAdapter(activity!!, ListItemTypeFactory())
        }
        itemsRv.adapter = adapter

        loadingRetryBtn.setOnClickListener {
            presenter.onLoadingRetryClicked()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemsRv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbar.isSelected = itemsRv.canScrollVertically(-1)
            }
        }
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun showLoadingOverlay() {
        loadingParentLl.visibility = View.VISIBLE
        loadingPb.visibility = View.VISIBLE
        loadingTv.visibility = View.VISIBLE
        loadingRetryBtn.visibility = View.GONE
    }

    override fun hideLoadingOverlay() {
        loadingParentLl.visibility = View.GONE
    }

    override fun showErrorOverlay() {
        loadingParentLl.visibility = View.VISIBLE
        loadingPb.visibility = View.GONE
        loadingTv.visibility = View.GONE
        loadingRetryBtn.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(containerRl, message, Snackbar.LENGTH_SHORT).show()
    }
}