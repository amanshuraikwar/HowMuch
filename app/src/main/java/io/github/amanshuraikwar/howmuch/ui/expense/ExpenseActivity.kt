package io.github.amanshuraikwar.howmuch.ui.expense

import android.accounts.Account
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_expense.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import java.util.*

class ExpenseActivity : BaseActivity<ExpenseContract.View, ExpenseContract.Presenter>(),
        ExpenseContract.View {

    private val tag = Util.getTag(this)

    private var transactionType = TransactionType.DEBIT

    companion object {
        const val KEY_TRANSACTION = "transaction"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        init()
    }

    override fun onBackPressed() {
        presenter.onBackBtnClicked()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentSv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbarCl.isSelected = contentSv.canScrollVertically(-1)
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
    }

    override fun showLoading(message: String) {
        loadingParentLl.visibility = View.VISIBLE
        loadingTv.text = message
    }

    override fun hideLoading() {
        loadingParentLl.visibility = View.GONE
        loadingTv.text = ""
    }

    override fun showError(message: String) {
    }

    override fun getGoogleAccountCredential(googleAccount: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(this, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(googleAccount)
    }

    override fun getTransaction(): Transaction {
        return intent.getParcelableExtra(KEY_TRANSACTION)
    }

    override fun setTransaction(transaction: Transaction) {
        intent.putExtra(KEY_TRANSACTION,  transaction)
    }

    override fun showTransaction(amount: String,
                                 transactionType: TransactionType,
                                 title: String,
                                 category: String,
                                 date: String,
                                 time: String,
                                 description: String?) {
        amountEt.setText(amount)

        this.transactionType = transactionType

        if (transactionType == TransactionType.DEBIT) {
            transactionTypeIb.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.red)
            )
            amountEt.setTextColor(ContextCompat.getColor(this, R.color.red))
            transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp)
        } else {
            transactionTypeIb.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.green)
            )
            amountEt.setTextColor(ContextCompat.getColor(this, R.color.green))
            transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp)
        }

        titleEt.setText(title)

        categorySp.setSelection(getCategoryIndex(category))

        categoryTv.text = category
        dateTv.text = date
        timeTv.text = time

        descriptionEt.setText(description)
    }

    private fun getCategoryIndex(category: String): Int {
        var i = 0
        while (i < categorySp.count) {
            if (categorySp.adapter.getItem(i).toString() == category) {
                return i
            }
            i++
        }
        return 0
    }

    override fun showEditMode() {

        backIb.setImageResource(R.drawable.ic_close_white_24dp)
        backIb.setOnClickListener {
            presenter.onEditCloseClicked()
        }

        editIb.visibility = GONE
        editIb.setOnClickListener(null)

        deleteIb.visibility = GONE
        deleteIb.setOnClickListener(null)

        doneBtn.visibility = VISIBLE
        doneBtn.setOnClickListener {
            presenter.onEditSaveClicked(
                    date = dateTv.text.toString(),
                    time = timeTv.text.toString(),
                    amount = amountEt.text.toString(),
                    title = titleEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    category = categorySp.selectedItem.toString(),
                    type = transactionType
            )
        }

        transactionTypeIb.isClickable = true
        transactionTypeIb.setOnClickListener {
            synchronized(transactionType) {
                if (transactionType == TransactionType.DEBIT) {
                    transactionType = TransactionType.CREDIT
                    transactionTypeIb.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.green)
                    )
                    amountEt.setTextColor(ContextCompat.getColor(this, R.color.green))
                    transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp)
                } else {
                    transactionType = TransactionType.DEBIT
                    transactionTypeIb.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.red)
                    )
                    amountEt.setTextColor(ContextCompat.getColor(this, R.color.red))
                    transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp)
                }
            }
        }

        amountEt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        titleEt.inputType = InputType.TYPE_CLASS_TEXT
        categoryTv.visibility = GONE
        categorySp.visibility = VISIBLE

        dateTv.isClickable = true
        dateTv.setOnClickListener {
            presenter.onDateClicked(dateTv.text.toString())
        }

        timeTv.isClickable = true
        timeTv.setOnClickListener {
            presenter.onTimeClicked(timeTv.text.toString())
        }

        descriptionEt.inputType = InputType.TYPE_CLASS_TEXT
        descriptionEt.clearFocus()
    }

    override fun hideEditMode() {

        backIb.setImageResource(R.drawable.ic_keyboard_backspace_white_24dp)
        backIb.setOnClickListener {
            presenter.onBackBtnClicked()
        }

        editIb.visibility = VISIBLE
        editIb.setOnClickListener {
            presenter.onEditBtnClicked()
        }

        deleteIb.visibility = VISIBLE
        deleteIb.setOnClickListener{
            presenter.onDeleteBtnClicked()
        }

        doneBtn.visibility = GONE
        doneBtn.setOnClickListener(null)

        transactionTypeIb.isClickable = false
        transactionTypeIb.setOnClickListener(null)

        amountEt.inputType = InputType.TYPE_NULL
        amountEt.clearFocus()
        titleEt.inputType = InputType.TYPE_NULL
        titleEt.clearFocus()
        categoryTv.visibility = VISIBLE
        categorySp.visibility = GONE

        dateTv.isClickable = false
        dateTv.setOnClickListener(null)

        timeTv.isClickable = false
        timeTv.setOnClickListener(null)

        descriptionEt.inputType = InputType.TYPE_NULL
        descriptionEt.clearFocus()
    }

    override fun showCategories(categories: List<String>) {
        categorySp.adapter = ArrayAdapter(this, R.layout.textview_spinner, categories)
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
        amountEt.error = message
    }

    override fun showTitleError(message: String) {
        titleEt.error = message
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

    @Module
    abstract class ExpenseModule {

        @Binds
        abstract fun presenter(presenter: ExpenseContract.ExpensePresenter): ExpenseContract.Presenter

        @Binds
        @ActivityContext
        abstract fun activity(activity: ExpenseActivity): AppCompatActivity
    }
}