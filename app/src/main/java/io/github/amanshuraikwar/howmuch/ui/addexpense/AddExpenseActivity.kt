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
import io.github.amanshuraikwar.howmuch.ui.spinner.WalletAdapter
import kotlinx.android.synthetic.main.activity_add_expense.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter

class AddExpenseActivity : BaseActivity<AddExpenseContract.View, AddExpenseContract.Presenter>()
        , AddExpenseContract.View {

    private lateinit var adapter: MultiItemListAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        init()
    }

    private fun init() {

        toolbar.setNavigationIcon(R.drawable.round_close_24)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentSv.setOnScrollChangeListener {
                _, _, _, _, _ ->
                toolbar.isSelected = contentSv.canScrollVertically(-1)
            }
        }


//        transactionTypeIb.setOnClickListener {
//            presenter.onTransactionTypeBtnClicked()
//        }

        titleEt.setOnFocusChangeListener {
            _, hasFocus ->
            if (hasFocus) {
                // show time animation
//                (titleIv.drawable as Animatable).start()
            }
        }

        titleEt.setOnClickListener {
            // show time animation
//            (titleIv.drawable as Animatable).start()
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

        saveBtn.setOnClickListener {
            presenter.onSaveClicked(
                    date = dateTv.text.toString(),
                    time = timeTv.text.toString(),
                    amount = amountEt.text.toString(),
                    title = titleEt.text.toString()
            )
        }

        adapter = MultiItemListAdapter(this, ListItemTypeFactory())
        categoryPicker.adapter = adapter
        categoryPicker.setSlideOnFling(true)

        categoryPicker.addOnItemChangedListener {
            _, position ->
            presenter.onCategoryChanged(position)
        }

        // cityPicker.addScrollStateChangeListener()
        categoryPicker.setItemTransitionTimeMillis(300)
        categoryPicker.setItemTransformer(
                ScaleTransformer
                        .Builder()
                        .setMinScale(0.8f)
                        .build()
        )
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
    }

    override fun showLoading(message: String) {
        pb.visibility = View.VISIBLE
        scrimV.visibility = View.VISIBLE
        saveBtn.isEnabled = false
        saveBtn.text = message
    }

    override fun hideLoading() {
        pb.visibility = View.GONE
        scrimV.visibility = View.GONE
        saveBtn.isEnabled = true
        saveBtn.text = "save"
    }

    override fun showCategories(categories: List<ListItem<*, *>>) {
        adapter.submitList(categories)
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
        amountTil.error = message
        amountEt.requestFocus()
    }

    override fun showTitleError(message: String) {
        amountTil.error = message
        titleEt.requestFocus()
    }

    override fun categorySelected(name: String,
                                  color: Int,
                                  color2: Int) {
        categoryNameTv.text = name
        categoryNameTv.setTextColor(ContextCompat.getColor(this, color))
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