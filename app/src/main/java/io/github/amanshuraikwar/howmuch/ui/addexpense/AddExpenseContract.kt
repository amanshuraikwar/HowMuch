package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.ExpenseDataInputView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.CategoryItem
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface AddExpenseContract {

    interface View : BaseView, UiMessageView, LoadingView, ExpenseDataInputView {
        fun showCategories(categories: List<ListItem<*, *>>)
        fun close(success: Boolean)
        fun showDatePicker(day: Int, month: Int, year: Int)
        fun showDate(date: String)
        fun showTime(time: String)
        fun showTimePicker(minute: Int, hourOfDay: Int)
        fun categorySelected(name: String,
                             color: Int,
                             color2: Int)
    }

    interface Presenter: BasePresenter<View> {
        fun onSaveClicked(date: String,
                          time: String,
                          amount: String,
                          title: String)
        fun onDateTvClicked(date: String)
        fun onTimeTvClicked(time: String)
        fun onDateSelected(dayOfMonth: Int, monthOfYear: Int, yearNo: Int)
        fun onTimeSelected(minute: Int, hourOfDay: Int)
        fun onBackIbPressed()
        fun onCategoryChanged(position: Int)
    }

    class AddExpensePresenter @Inject constructor(appBus: AppBus,
                                                  dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var categories: List<Category>
        private lateinit var wallets: List<Wallet>

        private lateinit var selectedCategory: Category

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            // show cur date and cur time
            getView()?.run {
                showDate(Util.beautifyDate(Util.getCurDate()))
                showTime(Util.beautifyTime(Util.getCurTime()))
            }

            refreshCategories()
        }

        private fun refreshCategories() {

            getDataManager()
                    .getAllCategories()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        categories ->
                        this.categories = categories.toList()
                        getView()?.showCategories(
                                categories
                                        .filter {
                                            it.type == TransactionType.DEBIT
                                        }
                                        .map {
                                            CategoryItem.Item(
                                                    CategoryItem(
                                                            ViewUtil.getCategoryIcon(it.name),
                                                            ViewUtil.getCategoryColor(it.name),
                                                            ViewUtil.getCategoryColor2(it.name)
                                                    )
                                            )
                                        }
                        )
                    }
                    .observeOn(Schedulers.newThread())
                    .flatMap {
                        getDataManager().getAllWallets()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Initialising...")
                    }
                    .subscribe(
                            {
                                wallets ->
                                this.wallets = wallets.toList()
                                getView()?.run {
                                    hideLoading()
                                }
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

        override fun onSaveClicked(date: String,
                                   time: String,
                                   amount: String,
                                   title: String) {

            if (amount.isEmpty()) {
                getView()?.showAmountError("Amount cannot be empty!")
                return
            }

            // if title is empty, get default transaction title
            val titleToSave =
                    if (title.isEmpty()) {
                        ViewUtil.getDefaultTransactionTitle(selectedCategory)
                    } else {
                        title
                    }

            getDataManager()
                    .addTransaction(
                            Transaction(
                                    id = "",
                                    date = Util.unBeautifyDate(date),
                                    time = Util.unBeautifyTime(time),
                                    amount = amount.toDouble(),
                                    title = titleToSave,
                                    description = "",
                                    categoryId = selectedCategory.id,
                                    type = selectedCategory.type,
                                    walletId = wallets[0].id
                            )
                    )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideLoading()
                                    close(true)
                                }
                            },
                            {
                                Log.e(tag, "onSaveClicked: addTransaction", it)

                                // todo handle specific errors

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
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

        override fun onCategoryChanged(position: Int) {
            selectedCategory = categories[position]
            getView()?.categorySelected(
                    selectedCategory.name,
                    ViewUtil.getCategoryColor(selectedCategory.name),
                    ViewUtil.getCategoryColor2(selectedCategory.name)
            )
        }
    }
}