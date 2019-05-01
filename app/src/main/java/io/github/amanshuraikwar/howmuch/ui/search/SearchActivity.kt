package io.github.amanshuraikwar.howmuch.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


class SearchActivity
@Inject constructor(): BaseActivity<SearchContract.View, SearchContract.Presenter>(), SearchContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TRANSACTION) {
            presenter.search("")
        }
    }

    private fun init() {

        itemsRv.layoutManager = LinearLayoutManager(this)

        if (adapter == null) {
            adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        backIb.setOnClickListener { dismiss() }

        setupSearchView()
    }

    private fun setupSearchView() {
        searchQueryEt.setOnEditorActionListener {
            v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.search(v.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun dismiss() {
        // clear the background else the touch ripple moves with the translation which looks bad
        backIb.background = null
        finishAfterTransition()
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {

    }

    override fun showLoading(message: String) {
        pb.visibility = VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    override fun startTransactionActivity(transaction: Transaction) {
        val intent = Intent(this, ExpenseActivity::class.java)
        intent.putExtra(ExpenseActivity.KEY_TRANSACTION, transaction)
        startActivityForResult(intent, REQ_CODE_TRANSACTION)
    }

    @Module
    abstract class DiModule {
        @Binds
        abstract fun presenter(presenter: SearchContract.PresenterImpl)
                : SearchContract.Presenter
    }
}