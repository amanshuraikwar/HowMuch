package io.github.amanshuraikwar.howmuch.ui.history

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.activity_monthly_budget.*

class HistoryActivity : BaseActivity<HistoryActivityContract.View,
        HistoryActivityContract.Presenter>(),
        HistoryActivityContract.View {

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    private var adapter: MultiItemListAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_budget)
        init()
    }

    private fun init() {

        toolbar.title = "History"
        pb.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))

        itemsRv.layoutManager = LinearLayoutManager(this)

        if (adapter == null) {
            adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        toolbar.inflateMenu(R.menu.refresh_navigation)
        toolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.refresh) {
                presenter.onRefreshClicked()
                return@setOnMenuItemClickListener true
            }

            return@setOnMenuItemClickListener false
        }

        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }

        previousMonthBtn.setOnClickListener {
            presenter.onPreviousMonthClicked()
        }

        nextMonthBtn.setOnClickListener {
            presenter.onNextMonthClicked()
        }
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun setSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(this, R.drawable.round_sync_problem_24)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(this, R.drawable.round_sync_24)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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

    override fun updateMonth(previousMonth: Boolean,
                             monthName: String,
                             nextMonth: Boolean) {
        previousMonthBtn.isEnabled = previousMonth
        curMonthTv.text = monthName
        nextMonthBtn.isEnabled = nextMonth
    }

    override fun startTransactionActivity(transaction: Transaction,
                                          category: Category) {
        startActivityForResult(
                {
                    val intent = Intent(this, ExpenseActivity::class.java)
                    intent.putExtra(ExpenseActivity.KEY_TRANSACTION, transaction)
                    intent.putExtra(ExpenseActivity.KEY_CATEGORY, category)
                    intent
                }.invoke(),
                REQ_CODE_TRANSACTION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onTransactionEdited()
            }
        }
    }

    override fun getInitDisplayedMonth(): Int? {
        return null
    }

    override fun getInitDisplayedYear(): Int? {
        return null
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: HistoryActivityContract.PresenterImpl): HistoryActivityContract.Presenter

        @Binds
        @ActivityContext
        abstract fun b(a: HistoryActivity): AppCompatActivity
    }
}