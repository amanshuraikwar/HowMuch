package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.accounts.Account
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_expense.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import java.util.*

class AddExpenseActivity : BaseActivity<AddExpenseContract.View, AddExpenseContract.Presenter>()
        , AddExpenseContract.View {

    private var transactionType = TransactionType.DEBIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        init()
    }

    private fun init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentSv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbarCl.isSelected = contentSv.canScrollVertically(-1)
            }
        }

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

        dateTv.setOnClickListener {
            presenter.onDateTvClicked(dateTv.text.toString())
        }

        timeTv.setOnClickListener {
            presenter.onTimeTvClicked(timeTv.text.toString())
        }

        doneBtn.setOnClickListener {
            presenter.onSaveClicked(
                    date = dateTv.text.toString(),
                    time = timeTv.text.toString(),
                    amount = amountEt.text.toString(),
                    title = titleEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    category = categorySp.selectedItem.toString(),
                    type = transactionType
            )
        }

        backIb.setOnClickListener {
            presenter.onBackIbPressed()
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

    override fun showDate(date: String) {
        dateTv.text = date
    }

    override fun showTime(time: String) {
        timeTv.text = time
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

    override fun showAmountError(message: String) {
        amountEt.error = message
        amountEt.requestFocus()
    }

    override fun showTitleError(message: String) {
        titleEt.error = message
        titleEt.requestFocus()
    }

    @Module
    abstract class AddTransactionModule {
        @Binds
        abstract fun presenter(presenter: AddExpenseContract.AddExpensePresenter): AddExpenseContract.Presenter

        @Binds
        @ActivityContext
        abstract fun activity(activity: AddExpenseActivity): AppCompatActivity
    }
}