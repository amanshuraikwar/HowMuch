package io.github.amanshuraikwar.howmuch.ui.stats

import android.accounts.Account
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_stats.*
import java.util.*
import javax.inject.Inject


class StatsFragment
@Inject constructor(): BaseFragment<StatsContract.View, StatsContract.Presenter>(), StatsContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
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
//                activity.toolbar()?.isSelected = itemsRv.canScrollVertically(-1)
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

    @Module
    abstract class StatsModule {
        @Binds
        abstract fun presenter(presenter: StatsContract.StatsPresenter): StatsContract.Presenter
    }
}