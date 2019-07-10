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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface AddExpenseContract {

    interface View : BaseView, UiMessageView, LoadingView, ExpenseDataInputView {
        fun showCategories(categories: List<ListItem<*, *>>)
        fun showDatePicker(day: Int, month: Int, year: Int)
        fun showDate(date: String)
        fun showTime(time: String)
        fun showTimePicker(minute: Int, hourOfDay: Int)
        fun categorySelected(name: String,
                             color: Int,
                             color2: Int)
        fun getMode(): AddExpenseFragment.Mode
        fun getTransaction(): Transaction?
        fun showAmount(amount: String)
        fun showTitle(title: String)
        fun showCategory(categoryIndex: Int)
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
        fun onCategoryChanged(position: Int)
    }

    class AddExpensePresenter @Inject constructor(appBus: AppBus,
                                                  dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var categories: MutableList<Category>
        private lateinit var wallets: List<Wallet>

        private lateinit var selectedCategory: Category
        private lateinit var transaction: Transaction
        private lateinit var mode: AddExpenseFragment.Mode

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {
            refreshCategories()
        }

        private fun refreshCategories() {

            getDataManager()
                    .getAllWallets()
                    .doOnNext{
                        this.wallets = it.toList()
                    }
                    .flatMap {
                        getDataManager().getAllCategories().map {
                            it.filter { it.type == TransactionType.DEBIT }
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Fetching categories...")
                    }
                    .subscribe(
                            {
                                categories ->
                                this.categories = categories.toMutableList()
                                initUi()
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
                                    getAppBus().onAddExpenseInitFailed.onNext(Any())
                                }
                            }
                    )
                    .addToCleanup()
        }

        private fun initUi() {

            getView()?.let {

                mode = it.getMode()

                if (mode == AddExpenseFragment.Mode.ADD) {
                    // show cur date and cur time
                    it.showDate(Util.beautifyDate(Util.getCurDate()))
                    it.showTime(Util.beautifyTime(Util.getCurTime()))
                } else {
                    transaction = it.getTransaction()!!
                    it.showAmount(transaction.amount.toString())
                    it.showTitle(transaction.title)
                    it.showDate(Util.beautifyDate(transaction.date))
                    it.showTime(Util.beautifyTime(transaction.time))

                    // adding selected category at top
                    val index = categories.indexOfFirst { it.id ==  transaction.categoryId}
                    val transactionCategory = categories[index]
                    categories.removeAt(index)
                    categories.add(0, transactionCategory)
                }

                it.showCategories(
                        categories
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
        }

        private fun getSuggestedTransactionTitle(amount: String,
                                                 time: String): String {

            if (selectedCategory.name.toLowerCase() == "food") {

                val hour = Util.unBeautifyTime(time).split(":")[0].toInt()

                if (hour in 7..11) {
                    return "Breakfast"
                }

                if (hour in 11..14) {
                    return "Lunch"
                }

                if (hour in 16..18) {
                    return "Snacks"
                }

                if (hour in 19..22) {
                    return "Dinner"
                }

                if (hour in 22..24) {
                    return "Groceries"
                }
            }

            if (selectedCategory.name.toLowerCase() == "home") {

                if (amount.toDouble() >= 10000) {
                    return "Rent"
                }
            }

            return selectedCategory.name

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
                        getSuggestedTransactionTitle(amount, time)
                    } else {
                        title
                    }

            val obs = {
                if (mode == AddExpenseFragment.Mode.ADD) {
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
                } else {
                    getDataManager()
                            .updateTransaction(
                                    Transaction(
                                            id = transaction.id,
                                            date = Util.unBeautifyDate(date),
                                            time = Util.unBeautifyTime(time),
                                            amount = amount.toDouble(),
                                            title = titleToSave,
                                            description = transaction.description,
                                            categoryId = selectedCategory.id,
                                            type = transaction.type,
                                            walletId = transaction.walletId
                                    )
                            )
                }
            }.invoke()

            obs
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideLoading()
                                    getAppBus().onAddExpenseProcessCompleted.onNext(Any())
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