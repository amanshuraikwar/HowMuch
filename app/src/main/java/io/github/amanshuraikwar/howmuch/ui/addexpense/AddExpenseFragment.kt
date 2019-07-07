package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_add_expense.*
import javax.inject.Inject

class AddExpenseFragment
@Inject constructor(): BaseFragment<AddExpenseContract.View, AddExpenseContract.Presenter>(), AddExpenseContract.View {

    companion object {
        // value -> ADD/EDIT
        const val KEY_MODE = "mode"
        const val KEY_TRANSACTION = "transaction"
    }

    @Parcelize
    enum class Mode : Parcelable {
        ADD, EDIT
    }

    private lateinit var adapter: MultiItemListAdapter<*>

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_expense, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

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

        adapter = MultiItemListAdapter(activity, ListItemTypeFactory())
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
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
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

    override fun showDatePicker(day: Int, month: Int, year: Int) {
        DatePickerDialog(
                activity,
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
                activity,
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

    override fun getMode(): Mode {
        return arguments?.getParcelable(KEY_MODE) ?: Mode.ADD
    }

    override fun getTransaction(): Transaction? {
        return arguments?.getParcelable(KEY_TRANSACTION)
    }

    override fun categorySelected(name: String,
                                  color: Int,
                                  color2: Int) {
        categoryNameTv.text = name
        categoryNameTv.setTextColor(ContextCompat.getColor(activity, color))
    }

    override fun showAmount(amount: String) {
        amountEt.setText(amount)
    }

    override fun showTitle(title: String) {
        titleEt.setText(title)
    }

    override fun showCategory(categoryIndex: Int) {
        // categoryPicker.scrollToPosition(categoryIndex)
    }

    @Module
    abstract class DiModule {
        @Binds
        abstract fun a(a: AddExpenseContract.AddExpensePresenter)
                : AddExpenseContract.Presenter
    }
}