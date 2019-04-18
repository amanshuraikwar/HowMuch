package io.github.amanshuraikwar.howmuch.ui.history

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HistoryContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startTransactionActivity(transaction: Transaction)
        fun setSyncError()
        fun clearSyncError()
        fun getFilters(): String?
    }

    interface Presenter : BasePresenter<View> {
        fun onTransactionEdited()
        fun onRefreshClicked()
    }

    class HistoryPresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private val transactionOnClickListener=
                object : TransactionOnClickListener {
            override fun onClick(transaction: Transaction) {
                getView()?.startTransactionActivity(transaction)
            }
        }

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                attachToAppBus()
                fetchTransactions()
            }
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onTransactionAdded
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fetchTransactions()
                    }
                    .addToCleanup()

        }

        private fun fetchTransactions() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it
                                .toList()
                                .filter(getView()?.getFilters())
                                .getListItems()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching transactions...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {
                                getView()?.run {
                                    hideLoading()
                                    submitList(it)
                                }

                            },
                            {
                                it.printStackTrace()
                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()


        }

        private fun List<Transaction>.filter(filterStr: String?): List<Transaction> {

            if (filterStr == null) {
                return this
            }

            val filters = filterStr.split("&")
            Log.i(tag, "filterStr: ${filters.size} filters found.")
            Log.i(tag, "filterStr: Filters = $filters")

            val transactionTypes = HashSet<TransactionType>()
            val categoryIds = HashSet<String>()
            val walletIds = HashSet<String>()

            filters.forEach {

                filter ->

                val filterParts = filter.split("=")

                if (filterParts.size == 2) {

                    when {

                        filterParts[0] == "transaction_type" -> {

                            val filterValues = filterParts[1].split("|")

                            filterValues.forEach {

                                when (it) {
                                    "DEBIT" -> transactionTypes.add(TransactionType.DEBIT)
                                    "CREDIT" -> transactionTypes.add(TransactionType.CREDIT)
                                    "ALL" -> {
                                        transactionTypes.add(TransactionType.CREDIT)
                                        transactionTypes.add(TransactionType.DEBIT)
                                    }
                                    else -> {
                                        Log.w(tag, "filter: Invalid transaction type $it found for filter.")
                                    }
                                }
                            }

                        }

                        filterParts[0] == "category_id" -> {

                            val filterValues = filterParts[1].split("|")
                            filterValues.forEach { categoryIds.add(it) }

                        }

                        filterParts[0] == "wallet_id" -> {

                            val filterValues = filterParts[1].split("|")
                            filterValues.forEach { walletIds.add(it) }

                        }
                    }

                } else {
                    Log.w(tag, "filterStr: Invalid filterStr $filter found.")
                }
            }

            return this.filter {

                var valid = true

                if (transactionTypes.isNotEmpty()) {
                    valid = valid && (it.type in transactionTypes)
                }

                if (categoryIds.isNotEmpty()) {
                    valid = valid && (it.categoryId in categoryIds)
                }

                if (walletIds.isNotEmpty()) {
                    valid = valid && (it.walletId in walletIds)
                }

                valid
            }
        }

        private fun List<Transaction>.getListItems(): List<ListItem<*, *>> {


            val inputSorted =
                    this
                            .sortedWith(
                                    Comparator {
                                        t1,t2 ->
                                        (Util.toTimeMillisec(t1.date, t1.time)
                                                - Util.toTimeMillisec(t2.date, t2.time)).toInt()
                                    }
                            )
                            .reversed()

            val list = mutableListOf<ListItem<*, *>>()

            var date = ""
            var i = 0
            while (i < inputSorted.size) {
                if (date != inputSorted[i].date) {
                    date = inputSorted[i].date
                    list.add(HeaderListItem(Util.beautifyDate(date).toUpperCase()))
                }
                list.add(TransactionListItem(inputSorted[i]).setOnClickListener(transactionOnClickListener))
                i += 1
            }
            if(list.size == 0) {
                list.add(EmptyListItem("O_O"))
            }

            return list
        }

        override fun onTransactionEdited() {
            fetchTransactions()
        }

        override fun onRefreshClicked() {
            fetchTransactions()
        }
    }
}