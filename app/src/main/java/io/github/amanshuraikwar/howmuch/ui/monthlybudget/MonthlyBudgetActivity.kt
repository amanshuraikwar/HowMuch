package io.github.amanshuraikwar.howmuch.ui.monthlybudget

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.category.CategoryActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.activity_monthly_budget.curMonthTv
import kotlinx.android.synthetic.main.activity_monthly_budget.itemsRv
import kotlinx.android.synthetic.main.activity_monthly_budget.nextMonthBtn
import kotlinx.android.synthetic.main.activity_monthly_budget.pb
import kotlinx.android.synthetic.main.activity_monthly_budget.previousMonthBtn
import kotlinx.android.synthetic.main.activity_monthly_budget.toolbar
import javax.inject.Inject


class MonthlyBudgetActivity
@Inject constructor() : BaseActivity<MonthlyBudgetContract.View,
        MonthlyBudgetContract.Presenter>(),
        MonthlyBudgetContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_budget)
        init()
    }

    private fun init() {

        itemsRv.layoutManager = LinearLayoutManager(this)

        if (adapter == null) {
            adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        toolbar.inflateMenu(R.menu.refresh_navigation)
        toolbar.setOnMenuItemClickListener {
            presenter.onRefreshClicked()
            return@setOnMenuItemClickListener true
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
        pb.visibility = VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    override fun startHistoryActivity(category: Category,
                                      month: Int,
                                      year: Int) {
        startActivity(
                {
                    val intent = Intent(this, CategoryActivity::class.java)
                    intent.putExtra("category", category)
                    intent.putExtra("init_month", month)
                    intent.putExtra("init_year", year)
                    intent
                }.invoke()

        )
    }

    override fun updateMonth(previousMonth: Boolean,
                             monthName: String,
                             nextMonth: Boolean) {
        previousMonthBtn.isEnabled = previousMonth
        curMonthTv.text = monthName
        nextMonthBtn.isEnabled = nextMonth
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: MonthlyBudgetContract.PresenterImpl)
                : MonthlyBudgetContract.Presenter
    }
}