package io.github.amanshuraikwar.howmuch.ui.categories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.category.CategoryActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_categories.*
import javax.inject.Inject


class CategoriesFragment
@Inject constructor(): BaseFragment<CategoriesContract.View, CategoriesContract.Presenter>(), CategoriesContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, null)
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

    override fun startHistoryActivity(category: Category,
                                      month: Int,
                                      year: Int) {
        startActivity(
                {
                    val intent = Intent(activity, CategoryActivity::class.java)
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

    @ColorInt
    override fun getCategoryColor(category: String): Int {
        return ContextCompat.getColor(
                activity,
                when(category.toLowerCase()) {
                    "food" -> R.color.food
                    "health/medical" -> R.color.health
                    "home" -> R.color.home
                    "transportation" -> R.color.transportation
                    "personal" -> R.color.personal
                    "utilities" -> R.color.utilities
                    "travel" -> R.color.travel
                    else -> R.color.activeIcon
                }
        )
    }

    @Module
    abstract class DiModule {
        @Binds
        abstract fun presenter(presenter: CategoriesContract.PresenterImpl)
                : CategoriesContract.Presenter
    }
}
