package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.CategoryAdapter
import io.github.amanshuraikwar.howmuch.ui.WalletAdapter
import kotlinx.android.synthetic.main.activity_add_expense.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*

class AddExpenseActivity : BaseActivity<AddExpenseContract.View, AddExpenseContract.Presenter>()
        , AddExpenseContract.View {

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
            presenter.onTransactionTypeBtnClicked()
        }

        titleEt.setOnFocusChangeListener {
            _, hasFocus ->
            if (hasFocus) {
                // show time animation
                (titleIv.drawable as Animatable).start()
            }
        }

        titleEt.setOnClickListener {
            // show time animation
            (titleIv.drawable as Animatable).start()
        }

        dateTv.setOnClickListener {
            presenter.onDateTvClicked(dateTv.text.toString())
        }

        timeIv.setOnClickListener {
            (timeIv.drawable as Animatable).start()
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
                    category = categorySp.selectedItem as Category,
                    wallet = walletSp.selectedItem as Wallet
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

    override fun showCategories(categories: List<Category>) {
        categorySp.adapter = CategoryAdapter(this, R.layout.textview_spinner, categories)
    }

    override fun showWallets(wallets: List<Wallet>) {
        walletSp.adapter = WalletAdapter(this, R.layout.textview_spinner, wallets)
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

                    // show time animation
                    (dateIv.drawable as Animatable).start()

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

                    // show time animation
                    (timeIv.drawable as Animatable).start()

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

    override fun switchToCredit() {

        val stateSet = intArrayOf(android.R.attr.state_checked * 1)
        transactionTypeIb.setImageState(stateSet, true)

        transactionTypeIb.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.green)
        )
        amountEt.setTextColor(ContextCompat.getColor(this, R.color.green))
//        transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp)
    }

    override fun switchToDebit() {

        val stateSet = intArrayOf(android.R.attr.state_checked * -1)
        transactionTypeIb.setImageState(stateSet, true)

        transactionTypeIb.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.red)
        )
        amountEt.setTextColor(ContextCompat.getColor(this, R.color.red))
//        transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp)
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