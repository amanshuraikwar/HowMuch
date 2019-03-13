package io.github.amanshuraikwar.howmuch.ui.addexpense

import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.ui.SheetsHelper
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface AddExpenseContract {

    interface View : BaseView, UiMessageView, LoadingView, GoogleAccountView {
        fun showCategories(categories: List<String>)
        fun close(success: Boolean)
        fun showDatePicker(day: Int, month: Int, year: Int)
        fun showDate(date: String)
        fun showTime(time: String)
        fun showTimePicker(minute: Int, hourOfDay: Int)
        fun showAmountError(message: String)
        fun showTitleError(message: String)
    }

    interface Presenter: BasePresenter<View> {
        fun onSaveClicked(date: String,
                          time: String,
                          amount: String,
                          title: String,
                          description: String,
                          category: String,
                          type: TransactionType)
        fun onDateTvClicked(date: String)
        fun onTimeTvClicked(time: String)
        fun onDateSelected(dayOfMonth: Int, monthOfYear: Int, yearNo: Int)
        fun onTimeSelected(minute: Int, hourOfDay: Int)
        fun onBackIbPressed()
    }

    class AddExpensePresenter @Inject constructor(appBus: AppBus,
                                                  dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        @Inject
        lateinit var sheetsHelper: SheetsHelper

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)

            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            // show cur date and cur time
            getView()?.showDate(Util.beautifyDate(Util.getCurDate()))
            getView()?.showTime(Util.beautifyTime(Util.getCurTime()))

            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMap {
                        spreadSheetId ->
                        sheetsHelper.fetchCategories(
                                spreadSheetId,
                                getView()?.getGoogleAccountCredential(getAccount()!!)!!
                        )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Fetching categories...")
                    }
                    .subscribe(
                            {
                                categories ->
                                getView()?.run {
                                    showCategories(categories)
                                    hideLoading()
                                }
                            },
                            {
                                it.printStackTrace()
                                // todo handle specific errors
                                getView()?.run {
                                    showToast(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                    hideLoading()
                                    close(false)
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onSaveClicked(date: String,
                                   time: String,
                                   amount: String,
                                   title: String,
                                   description: String,
                                   category: String,
                                   type: TransactionType) {

            if (amount.isEmpty()) {
                getView()?.showAmountError("Amount cannot be empty!")
                return
            }

            if (title.isEmpty()) {
                getView()?.showTitleError("Title cannot be empty!")
                return
            }

            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMapCompletable {
                        spreadSheetId ->
                        sheetsHelper
                                .addTransaction(
                                        transaction = Transaction(
                                                id = "",
                                                date = Util.unBeautifyDate(date),
                                                time = Util.unBeautifyTime(time),
                                                currency = getDataManager().getCurrency(),
                                                amount = amount.toDouble(),
                                                title = title,
                                                description = description,
                                                category = category,
                                                type = type,
                                                cellRange = ""
                                        ),
                                        spreadsheetId = spreadSheetId,
                                        googleAccountCredential = getView()?.getGoogleAccountCredential(getAccount()!!)!!
                                )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {

                                getView()?.hideLoading()
                                getView()?.close(true)
                            },
                            {
                                it.printStackTrace()
                                getView()?.hideLoading()
                                getView()?.showToast("Error!")
                            }
                    )
                    .addToCleanup()

        }

        override fun onDateTvClicked(date: String) {
            val dateParts = Util.getDateParts(Util.unBeautifyDate(date))
            getView()?.showDatePicker(dateParts[0], dateParts[1], dateParts[2])
        }

        override fun onTimeTvClicked(time: String) {
            val timeParts = Util.getTimeParts(Util.unBeautifyTime(time))
            getView()?.showTimePicker(timeParts[0], timeParts[1])
        }

        override fun onDateSelected(dayOfMonth: Int, monthOfYear: Int, yearNo: Int) {
            getView()?.showDate(Util.beautifyDate(Util.getDate(dayOfMonth, monthOfYear, yearNo)))
        }

        override fun onTimeSelected(minute: Int, hourOfDay: Int) {
            getView()?.showTime(Util.beautifyTime(Util.getTime(minute, hourOfDay)))
        }

        override fun onBackIbPressed() {
            getView()?.close(false)
        }
    }
}