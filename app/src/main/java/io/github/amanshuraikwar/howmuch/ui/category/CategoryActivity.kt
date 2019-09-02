package io.github.amanshuraikwar.howmuch.ui.category

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.activity_monthly_budget.curMonthTv
import kotlinx.android.synthetic.main.activity_monthly_budget.itemsRv
import kotlinx.android.synthetic.main.activity_monthly_budget.nextMonthBtn
import kotlinx.android.synthetic.main.activity_monthly_budget.pb
import kotlinx.android.synthetic.main.activity_monthly_budget.previousMonthBtn
import kotlinx.android.synthetic.main.activity_monthly_budget.toolbar
import kotlinx.android.synthetic.main.dialog_edit_category.view.*
import javax.inject.Inject


class CategoryActivity
@Inject constructor() : BaseActivity<CategoryContract.View,
        CategoryContract.Presenter>(),
        CategoryContract.View {

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    private var adapter: MultiItemListAdapter<*>? = null

    private lateinit var editDialogView: View
    private lateinit var editCategoriesDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_budget)
        init()
    }

    private fun init() {

        editDialogView = layoutInflater.inflate(R.layout.dialog_edit_category, null)
        editDialogView.saveBtn.setOnClickListener {
            presenter.onEditSaveClicked(editDialogView.monthlyLimitEt.text.toString())
        }
        editCategoriesDialog =
                MaterialAlertDialogBuilder(this).setView(editDialogView).create()

        itemsRv.layoutManager = LinearLayoutManager(this)

        if (adapter == null) {
            adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        toolbar.inflateMenu(R.menu.category_navigation)
        toolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.refresh) {
                presenter.onRefreshClicked()
                return@setOnMenuItemClickListener true
            }

            if (it.itemId == R.id.edit) {
                presenter.onEditClicked()
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
        toolbar.menu.getItem(1).icon =
                ContextCompat.getDrawable(this, R.drawable.round_sync_problem_24)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(1).icon =
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

    override fun getCategory(): Category? {
        return intent?.extras?.getParcelable("category")
    }

    override fun updateMonth(previousMonth: Boolean,
                             monthName: String,
                             nextMonth: Boolean) {
        previousMonthBtn.isEnabled = previousMonth
        curMonthTv.text = monthName
        nextMonthBtn.isEnabled = nextMonth
    }

    override fun initUi(title: String, color1: Int, color2: Int) {
        val actColor1 = ContextCompat.getColor(this, color1)
        toolbar.title = title
        pb.indeterminateTintList = ColorStateList.valueOf(actColor1)
        editDialogView.monthlyLimitTil.boxStrokeColor = actColor1
        editDialogView.monthlyLimitTil.hintTextColor = ColorStateList.valueOf(actColor1)
        editDialogView.saveBtn.setBackgroundColor(actColor1)

    }

    override fun showEditDialog(category: Category) {
        editDialogView.monthlyLimitEt.setText(category.monthlyLimit.toString())
        editDialogView.monthlyLimitEt.isEnabled = true
        editDialogView.monthlyLimitTil.error = null
        editDialogView.saveBtn.isEnabled = true
        editCategoriesDialog.show()
    }

    override fun hideEditDialog() {
        editCategoriesDialog.cancel()
    }

    override fun showEditDialogError(msg: String) {
        editDialogView.monthlyLimitTil.error = msg
    }

    override fun showEditDialogLoading(msg: String) {
        editDialogView.monthlyLimitEt.isEnabled = false
        editDialogView.saveBtn.isEnabled = false
        editDialogView.monthlyLimitTil.error = null
        editDialogView.saveBtn.text = msg
    }

    override fun hideEditDialogLoading() {
        editDialogView.monthlyLimitEt.isEnabled = true
        editDialogView.saveBtn.isEnabled = true
        editDialogView.saveBtn.text = "save"
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
        return intent?.extras?.getInt("init_month")?.let {
            return if (it > 0) it else null
        }
    }

    override fun getInitDisplayedYear(): Int? {
        return intent?.extras?.getInt("init_year")?.let {
            return if (it > 0) it else null
        }
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: CategoryContract.PresenterImpl)
                : CategoryContract.Presenter
    }
}