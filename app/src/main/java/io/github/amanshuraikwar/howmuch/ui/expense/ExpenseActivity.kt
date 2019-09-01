package io.github.amanshuraikwar.howmuch.ui.expense

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseFragment
import io.github.amanshuraikwar.howmuch.ui.category.CategoryActivity
import kotlinx.android.synthetic.main.activity_expense.*

class ExpenseActivity
    : BaseActivity<ExpenseContract.View, ExpenseContract.Presenter>(), ExpenseContract.View {

    companion object {
        const val KEY_TRANSACTION = "transaction"
        const val KEY_CATEGORY = "category"
    }

    private lateinit var curEditTransactionFragment: AddExpenseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        init()
    }

    override fun onBackPressed() {
        if (confirmTv.visibility == VISIBLE) {
            confirmTv.performClick()
        } else {
            presenter.onBackBtnClicked()
        }
    }

    private fun init() {
        toolbar.setNavigationIcon(R.drawable.round_close_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        deleteBtn.setOnClickListener {
            if (confirmTv.visibility == VISIBLE) {
                presenter.onDeleteConfirmedClicked()
            } else {
                confirmTv.visibility = VISIBLE
                editBtn.visibility = GONE
            }
        }
        confirmTv.setOnClickListener {
            confirmTv.visibility = GONE
            editBtn.visibility = VISIBLE
        }
        editBtn.setOnClickListener {
            presenter.onEditBtnClicked()
        }
        categoryCv.setOnClickListener {
            presenter.onCategoryClicked()
        }
        categoryTv.setOnClickListener {
            presenter.onCategoryClicked()
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
    }

    override fun showLoading(message: String) {
        pb.visibility = VISIBLE
        scrimV.visibility = VISIBLE
        editBtn.isEnabled = false
        deleteBtn.isEnabled = false
    }

    override fun hideLoading() {
        pb.visibility = GONE
        scrimV.visibility = INVISIBLE
        editBtn.isEnabled = true
        deleteBtn.isEnabled = true
    }

    override fun getTransaction(): Transaction {
        return intent.getParcelableExtra(KEY_TRANSACTION)
    }

    override fun getCategory(): Category {
        return intent.getParcelableExtra(KEY_CATEGORY)
    }

    override fun setTransaction(transaction: Transaction) {
        intent.putExtra(KEY_TRANSACTION,  transaction)
    }

    override fun showTransaction(amount: String,
                                 title: String,
                                 category: Category,
                                 date: String,
                                 time: String) {

        amountTv.text = amount
        titleTv.text = title
        categoryIv.setImageResource(ViewUtil.getCategoryIcon(category.name))
        categoryTv.text = category.name
        dateTv.text = date
        timeTv.text = time

        ContextCompat.getColor(this, ViewUtil.getCategoryColor3(category.name)).let {
            lineV.setBackgroundColor(it)
            editBtn.rippleColor = ColorStateList.valueOf(it)
        }
        lineV.setBackgroundColor(
                ContextCompat.getColor(this, ViewUtil.getCategoryColor2(category.name)))

        ContextCompat.getColor(this, ViewUtil.getCategoryColor(category.name)).let {
            amountCv.setCardBackgroundColor(it)
            titleCv.setCardBackgroundColor(it)
            categoryCv.setCardBackgroundColor(it)
            dateCv.setCardBackgroundColor(it)
            timeCv.setCardBackgroundColor(it)
            editBtn.setBackgroundColor(it)
        }
    }

    override fun showEditMode(transaction: Transaction) {

        curEditTransactionFragment = AddExpenseFragment()
        val args = Bundle()

        args.putParcelable(AddExpenseFragment.KEY_MODE, AddExpenseFragment.Mode.EDIT)
        args.putParcelable(AddExpenseFragment.KEY_TRANSACTION, transaction)
        curEditTransactionFragment.arguments = args

        supportFragmentManager.beginTransaction()
                .replace(R.id.editTransactionFl, curEditTransactionFragment)
                .commitAllowingStateLoss()

        editTransactionFl.visibility = VISIBLE
        transactionCl.visibility = GONE

        toolbar.title = "Edit transaction"
    }

    override fun hideEditMode() {

        supportFragmentManager
                .beginTransaction()
                .remove(curEditTransactionFragment)
                .commitAllowingStateLoss()

        editTransactionFl.visibility = GONE
        transactionCl.visibility = VISIBLE
        toolbar.title = "Transaction"
    }

    override fun close(success: Boolean) {
        setResult(if(success) Activity.RESULT_OK else Activity.RESULT_CANCELED)
        finish()
    }

    override fun showDatePicker(day: Int, month: Int, year: Int) {
        DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener {
                    _, yearNo, monthOfYear, dayOfMonth ->
                    presenter.onDateSelected(dayOfMonth, monthOfYear, yearNo)
                },
                year,
                month,
                day
        ).show()
    }

    override fun showTimePicker(minute: Int, hourOfDay: Int) {
        TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener {
                    _, hourOfDayNo, minuteNo ->
                    presenter.onTimeSelected(minuteNo, hourOfDayNo)
                },
                hourOfDay,
                minute,
                false
        ).show()
    }

    override fun showDate(date: String) {
        dateTv.text = date
    }

    override fun showTime(time: String) {
        timeTv.text = time
    }

    override fun showEditCloseDialog() {
        AlertDialog
                .Builder(this)
                .setMessage(R.string.edit_close_message)
                .setNegativeButton("Discard") {
                    dialog, _ ->
                    presenter.onEditDiscardClicked()
                    dialog.dismiss()
                }
                .setPositiveButton("Keep editing") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun showAmountError(message: String) {
    }

    override fun showTitleError(message: String) {
    }

    override fun showDeleteDialog() {
        AlertDialog
                .Builder(this)
                .setMessage(R.string.delete_confirm)
                .setNegativeButton("Delete") {
                    dialog, _ ->
                    presenter.onDeleteConfirmedClicked()
                    dialog.dismiss()
                }
                .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun switchToCredit() {

    }

    override fun switchToDebit() {

    }

    override fun showCategories(categories: List<Category>) {

    }

    override fun isInEditMode(): Boolean {
        return editTransactionFl.visibility == VISIBLE
    }

    override fun startHistoryActivity(category: Category) {
        startActivity(
                {
                    val intent = Intent(this, CategoryActivity::class.java)
                    intent.putExtra("category", category)
                    intent
                }.invoke()

        )
    }

    @Module
    abstract class ExpenseModule {

        @Binds
        abstract fun presenter(presenter: ExpenseContract.ExpensePresenter): ExpenseContract.Presenter

        @Binds
        @ActivityContext
        abstract fun activity(activity: ExpenseActivity): AppCompatActivity
    }
}
