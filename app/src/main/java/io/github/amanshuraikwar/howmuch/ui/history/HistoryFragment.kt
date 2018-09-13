package io.github.amanshuraikwar.howmuch.ui.history

import android.accounts.Account
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*
import javax.inject.Inject

class HistoryFragment
@Inject constructor(): BaseFragment<HistoryContract.View, HistoryContract.Presenter>(), HistoryContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (adapter == null) {
            adapter = MultiItemListAdapter(activity!!, ListItemTypeFactory())
            itemsRv.adapter = adapter
        }
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(activity, Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }
}