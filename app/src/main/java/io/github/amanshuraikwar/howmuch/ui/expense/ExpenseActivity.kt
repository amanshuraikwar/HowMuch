package io.github.amanshuraikwar.howmuch.ui.expense

import android.accounts.Account
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_expense.*
import java.util.*

class ExpenseActivity : BaseActivity<ExpenseContract.View, ExpenseContract.Presenter>(), ExpenseContract.View {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    companion object {
        const val KEY_EXPENSE = "expense"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        ButterKnife.bind(this)
        init()
    }

    private fun init() {

        submitBtn.setOnClickListener {
            presenter.onSubmitClicked(
                    Expense("",
                            Util.unBeautifyDate(dateTv.text.toString()),
                            Util.unbeautifyTime(getCurExpense().time),
                            amountEt.text.toString(),
                            descriptionEt.text.toString(),
                            categorySp.selectedItem.toString(),
                            getCurExpense().cellRange)
            )
        }

        val datePickerDialog =
                DatePickerDialog(
                        this,
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            dateTv.text =
                                    Util.beautifyDate(Util.getDate(dayOfMonth, monthOfYear, year))
                        },
                        Util.getYear(),
                        Util.getMonthOfYear(),
                        Util.getDayOfMonth()
                )

        dateTv.setOnClickListener {
            datePickerDialog.show()
        }

        closeIb.setOnClickListener {
            finish()
        }
    }

    override fun getCurExpense(): Expense {
        return intent.extras.getParcelable(KEY_EXPENSE)
    }

    override fun getGoogleAccountCredential(account: Account): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(this, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(account)
    }

    override fun populateCategories(categories: List<String>) {
        categorySp.adapter = ArrayAdapter(this, R.layout.textview_spinner, categories)
    }

    override fun setSubmitBtnText(text: String) {
        submitBtn.text = text
    }

    override fun enableSubmitBtn() {
        submitBtn.isEnabled = true
    }

    override fun disableSubmitBtn() {
        submitBtn.isEnabled = false
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(contentFl, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showAmountError(message: String) {
        amountEt.error = message
    }

    override fun showDescriptionError(message: String) {
        descriptionEt.error = message
    }

    override fun initUi(date: String, amount: String, category: String, description: String) {
        dateTv.text = date
        amountEt.setText(amount)
        categorySp.setSelection((categorySp.adapter as ArrayAdapter<String>).getPosition(category))
        descriptionEt.setText(description)
    }
}
