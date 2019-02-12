package io.github.amanshuraikwar.howmuch.ui.expense

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.ui.SheetsHelper
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

interface ExpenseContract {

    interface View : BaseView, UiMessageView, LoadingView, GoogleAccountView {
        fun getTransaction(): Transaction
        fun setTransaction(transaction: Transaction)
        fun showTransaction(amount: String,
                            transactionType: TransactionType,
                            title: String,
                            category: String,
                            date: String,
                            time: String,
                            description: String?)
        fun showEditMode()
        fun hideEditMode()
        fun showCategories(categories: List<String>)
        fun close(success: Boolean)
        fun showDatePicker(day: Int, month: Int, year: Int)
        fun showTimePicker(minute: Int, hourOfDay: Int)
        fun showDate(date: String)
        fun showTime(time: String)
        fun showEditCloseDialog()
        fun showAmountError(message: String)
        fun showTitleError(message: String)
        fun showDeleteDialog()
    }

    interface Presenter : BasePresenter<View> {
        fun onEditCloseClicked()
        fun onDateClicked(date: String)
        fun onBackBtnClicked()
        fun onTimeClicked(time: String)
        fun onDateSelected(dayOfMonth: Int, monthOfYear: Int, yearNo: Int)
        fun onTimeSelected(minuteNo: Int, hourOfDayNo: Int)
        fun onEditBtnClicked()
        fun onEditSaveClicked(date: String,
                              time: String,
                              amount: String,
                              title: String,
                              description: String,
                              category: String,
                              type: TransactionType)

        fun onEditDiscardClicked()
        fun onDeleteBtnClicked()
        fun onDeleteComfirmedClicked()
    }

    class ExpensePresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        @Inject
        lateinit var sheetsHelper: SheetsHelper

        private lateinit var curTransaction: Transaction

        private var updated: Boolean = false

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)

            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

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
                    .subscribe(
                            {
                                categories  ->
                                getView()?.showCategories(categories)
                                initUi()
                            },
                            {
                                it.printStackTrace()
                                getView()?.showToast(it.message ?: "Something went wrong!")
                                getView()?.close(updated)
                                // todo handle errors
                            }
                    )
                    .addToCleanup()


        }

        @Throws(Exception::class)
        private fun initUi() {
            curTransaction = getView()?.getTransaction() ?: throw Exception("Transaction not found!")
            showTransactionData()
            getView()?.hideEditMode()
        }

        @Throws(Exception::class)
        private fun showTransactionData() {
            getView()?.showTransaction(
                    curTransaction.amount.toString(),
                    curTransaction.type,
                    curTransaction.title,
                    curTransaction.category,
                    Util.beautifyDate(curTransaction.date),
                    Util.beautifyTime(curTransaction.time),
                    if (curTransaction.description == "") null else curTransaction.description
            )
        }

        override fun onEditCloseClicked() {
            getView()?.showEditCloseDialog()
        }

        override fun onDateClicked(date: String) {
            val dateParts = Util.getDateParts(Util.unBeautifyDate(date))
            getView()?.showDatePicker(dateParts[0], dateParts[1], dateParts[2])
        }

        override fun onBackBtnClicked() {
            getView()?.close(updated)
        }

        override fun onTimeClicked(time: String) {
            val timeParts = Util.getTimeParts(Util.unBeautifyTime(time))
            getView()?.showTimePicker(timeParts[0], timeParts[1])
        }

        override fun onDateSelected(dayOfMonth: Int, monthOfYear: Int, yearNo: Int) {
            getView()?.showDate(Util.beautifyDate(Util.getDate(dayOfMonth, monthOfYear, yearNo)))
        }

        override fun onTimeSelected(minuteNo: Int, hourOfDayNo: Int) {
            getView()?.showTime(Util.beautifyTime(Util.getTime(minuteNo, hourOfDayNo)))
        }

        override fun onEditBtnClicked() {
            showTransactionData()
            getView()?.showEditMode()
        }

        override fun onEditSaveClicked(date: String,
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

            val newTransaction = Transaction(
                    id = "",
                    date = Util.unBeautifyDate(date),
                    time = Util.unBeautifyTime(time),
                    currency = "",
                    amount = amount.toDouble(),
                    title = title,
                    description = description,
                    category = category,
                    type = type,
                    cellRange = curTransaction.cellRange
            )

            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMapCompletable {
                        spreadSheetId ->
                        sheetsHelper
                                .updateTransaction(
                                        newTransaction,
                                        spreadSheetId,
                                        getView()?.getGoogleAccountCredential(
                                                getAccount()!!
                                        )!!
                                )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.setTransaction(newTransaction)
                                initUi()
                                getView()?.showSnackbar("Transaction updated!")
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                getView()?.showToast(it.message ?: "Something went wrong!")
                                getView()?.hideLoading()
                                // todo handle errors
                            }
                    )
                    .addToCleanup()

        }

        override fun onEditDiscardClicked() {
            showTransactionData()
            getView()?.hideEditMode()
        }

        override fun onDeleteBtnClicked() {
            getView()?.showDeleteDialog()
        }

        override fun onDeleteComfirmedClicked() {
            getDataManager()
                    .getSpreadsheetIdForEmail(getEmail()!!)
                    .flatMapCompletable {
                        id ->
                        sheetsHelper
                                .deleteTransaction(
                                        curTransaction,
                                        id,
                                        getView()?.getGoogleAccountCredential(
                                                getAccount()!!
                                        )!!
                                )
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Deleting...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.close(updated)
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                getView()?.showToast(it.message ?: "Something went wrong!")
                                getView()?.hideLoading()
                                // todo handle errors
                            }
                    )
                    .addToCleanup()

        }
    }
}