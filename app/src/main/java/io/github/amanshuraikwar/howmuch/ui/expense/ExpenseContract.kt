package io.github.amanshuraikwar.howmuch.ui.expense

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.ExpenseDataInputView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

interface ExpenseContract {

    interface View : BaseView, UiMessageView, LoadingView, ExpenseDataInputView {
        fun getTransaction(): Transaction
        fun setTransaction(transaction: Transaction)
        fun showWallets(wallets: List<Wallet>)
        fun showTransaction(amount: String,
                            transactionType: TransactionType,
                            title: String,
                            category: Category,
                            wallet: Wallet,
                            date: String,
                            time: String,
                            description: String?,
                            categories: List<Category>)
        fun showEditMode()
        fun hideEditMode()
        fun close(success: Boolean)
        fun showDatePicker(day: Int, month: Int, year: Int)
        fun showTimePicker(minute: Int, hourOfDay: Int)
        fun showDate(date: String)
        fun showTime(time: String)
        fun showEditCloseDialog()
        fun showDeleteDialog()
        fun switchToCredit()
        fun switchToDebit()
        fun showCategories(categories: List<Category>)
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
                              category: Category,
                              wallet: Wallet)
        fun onEditDiscardClicked()
        fun onDeleteBtnClicked()
        fun onDeleteConfirmedClicked()
        fun onTransactionTypeBtnClicked()
    }

    class TransactionNotFoundException(msg: String) : Exception(msg)
    class InvalidCategoryException(msg: String) : Exception(msg)

    class ExpensePresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var curTransaction: Transaction
        private lateinit var categories: List<Category>
        private lateinit var wallets: List<Wallet>

        private lateinit var curTransactionType : TransactionType

        private var updated: Boolean = false

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            getDataManager()
                    .getAllCategories()
                    .doOnNext {
                        this.categories = it.toList()
                    }
                    .flatMap {
                        getDataManager().getAllWallets()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Fetching categories...")
                    }
                    .subscribe(
                            {
                                wallets ->
                                this.wallets = wallets.toList()
                                getView()?.run {
                                    hideLoading()
                                    showWallets(this@ExpensePresenter.wallets)
                                }
                                initTransaction()
                            },
                            {
                                it.printStackTrace()
                                Log.e(tag, "refreshCategories: getAllCategories", it)

                                // todo handle specific errors

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                    close(false)
                                }
                            }
                    )
                    .addToCleanup()
        }

        @Throws(TransactionNotFoundException::class)
        private fun initTransaction() {
            curTransaction =
                    getView()?.getTransaction()
                            ?: throw TransactionNotFoundException(
                                    "Transaction was not returned from the view!"
                            )
            curTransactionType = curTransaction.type
            curTransaction.show()
            getView()?.hideEditMode()
        }

        @Throws(InvalidCategoryException::class)
        private fun Transaction.show() {
            getView()?.showTransaction(
                    this.amount.toString(),
                    this.type,
                    this.title,
                    categories.find { it.id == this.categoryId }
                            ?: throw InvalidCategoryException(
                                    "Invalid category with id ${this.categoryId} " +
                                            "for transaction with id ${this.id}!"
                            ),
                    wallets.find { it.id == this.walletId }
                            ?: throw InvalidCategoryException(
                                    "Invalid wallet with id ${this.walletId} " +
                                            "for transaction with id ${this.id}!"
                            ),
                    Util.beautifyDate(this.date),
                    Util.beautifyTime(this.time),
                    this.description,
                    categories.filter { it.type == this.type }
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
            getView()?.showEditMode()
        }

        override fun onEditSaveClicked(date: String,
                                       time: String,
                                       amount: String,
                                       title: String,
                                       description: String,
                                       category: Category,
                                       wallet: Wallet) {

            if (amount.isEmpty()) {
                getView()?.showAmountError("Amount cannot be empty!")
                return
            }

            if (title.isEmpty()) {
                getView()?.showTitleError("Title cannot be empty!")
                return
            }

            val newTransaction = Transaction(
                    id = curTransaction.id,
                    date = Util.unBeautifyDate(date),
                    time = Util.unBeautifyTime(time),
                    amount = amount.toDouble(),
                    title = title,
                    description = description,
                    categoryId = category.id,
                    type = curTransactionType,
                    walletId = wallet.id
            )

            getDataManager()
                    .updateTransaction(newTransaction)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.setTransaction(newTransaction)
                                initTransaction()
                                getView()?.showSnackbar("Transaction updated!")
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                Log.e(tag, "onEditSaveClicked: updateTransaction", it)

                                // todo handle specific errors

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
                            }
                    )
                    .addToCleanup()

        }

        override fun onEditDiscardClicked() {
            curTransaction.show()
            getView()?.hideEditMode()
        }

        override fun onDeleteBtnClicked() {
            getView()?.showDeleteDialog()
        }

        override fun onDeleteConfirmedClicked() {
            getDataManager()
                    .deleteTransaction(curTransaction)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Deleting...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.run {
                                    close(updated)
                                    hideLoading()
                                }
                            },
                            {
                                it.printStackTrace()
                                Log.e(tag, "onDeleteConfirmedClicked: deleteTransaction", it)

                                // todo handle specific error codes

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onTransactionTypeBtnClicked() {
            synchronized(curTransactionType) {
                if (curTransactionType == TransactionType.DEBIT) {
                    curTransactionType = TransactionType.CREDIT
                    getView()?.switchToCredit()
                    getView()?.showCategories(categories.filter { it.type == TransactionType.CREDIT })
                } else {
                    curTransactionType = TransactionType.DEBIT
                    getView()?.switchToDebit()
                    getView()?.showCategories(categories.filter { it.type == TransactionType.DEBIT })
                }
            }
        }
    }
}