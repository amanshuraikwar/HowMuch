package io.github.amanshuraikwar.howmuch.ui.history

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.date.DateHeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.Graph
import io.github.amanshuraikwar.howmuch.ui.list.items.Total
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HistoryContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startTransactionActivity(transaction: Transaction)
        fun getFilters(): String?
    }

    interface Presenter : BasePresenter<View> {
        fun onTransactionEdited()
        fun onRefreshClicked()
        fun onAllTimeChecked()
        fun onThisMonthChecked()
        fun onThisWeekChecked()
    }

    class HistoryPresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        enum class TransactionTimeRange {
            ALL_TIME, THIS_MONTH, THIS_WEEK
        }

        private var curTransactionTimeRange = TransactionTimeRange.THIS_MONTH
        private var filtersApplicable = true
        private val transactionTypes = HashSet<TransactionType>()
        private val categoryIds = HashSet<String>()
        private val walletIds = HashSet<String>()

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
                setupFilters()
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

        private fun setupFilters() {

            val filterStr = getView()?.getFilters()

            // if filter string is itself null then return
            if (filterStr == null) {
                filtersApplicable = false
                return
            } else {
                filtersApplicable = true
            }

            // split filter types
            val filters = filterStr.split("&")
            Log.i(tag, "filterStr: ${filters.size} filters found.")
            Log.i(tag, "filterStr: Filters = $filters")

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
                                        Log.w(tag, "applyAttributeFilters: Invalid transaction type $it found for applyAttributeFilters.")
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
        }

        private fun fetchTransactions() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList().applyTimeFilter().applyAttributeFilters()
                    }
                    .map {

                        val list = mutableListOf<ListItem<*, *>>()

                        if (it.isNotEmpty()) {
                            it.getGraphListItem()?.run{
                                list.add(this)
                            }
                        }

                        if (it.isNotEmpty()) {
                            it.getTotalListItem()?.run{
                                list.add(this)
                            }
                        }

                        list.addAll(it.getListItems())

                        list
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

        private fun List<Transaction>.applyTimeFilter(): List<Transaction> {

            var list = this

            if (curTransactionTimeRange == TransactionTimeRange.THIS_MONTH) {

                val firstDayOfMonth = Util.getFirstDayOfMonth()

                list = list.filter {
                    Util.toTimeMillisec(it.date, it.time) > firstDayOfMonth
                }

            } else if (curTransactionTimeRange == TransactionTimeRange.THIS_WEEK) {

                val lastSeventhDay = Util.getLastSeventhDay()

                list = list.filter {
                    Util.toTimeMillisec(it.date, it.time) > lastSeventhDay
                }
            }

            return list
        }

        private fun List<Transaction>.applyAttributeFilters(): List<Transaction> {

            if (!filtersApplicable) {
                return this
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

        private fun List<Transaction>.getGraphListItem(): ListItem<*, *>? {

            // if transactions are for all time, don't show graph
            if (curTransactionTimeRange == TransactionTimeRange.ALL_TIME) {
                return null
            }

            // if both debit and credit transactions are to be shown
            // don't show the graph
            if (transactionTypes.size == 2) {
                return null
            }

            val txnsByType = this.groupBy { it.type }

            val debitDayAmountMap = linkedMapOf<String, Double>()
            val creditDayAmountMap = linkedMapOf<String, Double>()

            // initiate debit and credit day amount map for each day of past 7 days
            if (curTransactionTimeRange == TransactionTimeRange.THIS_WEEK) {

                var i = 0
                while (i >= -7) {
                    debitDayAmountMap[Util.getDateStrMoved(--i)] = 0.0
                    creditDayAmountMap[Util.getDateStrMoved(--i)] = 0.0
                }
            }

            // initiate debit and credit day amount map for each day of month
            if (curTransactionTimeRange == TransactionTimeRange.THIS_MONTH) {

                val monthFirstDate = Util.getFirstDateOfMonth()
                val curTimeMillisec = Util.getCurTimeMillisec()

                var i = -1
                while (Util.getDateMoved(monthFirstDate, ++i).time <= curTimeMillisec) {
                    debitDayAmountMap[Util.getDateStrMoved(monthFirstDate, i)] = 0.0
                    creditDayAmountMap[Util.getDateStrMoved(monthFirstDate, i)] = 0.0
                }
            }

            // filling debit amount in day amount map
            txnsByType[TransactionType.DEBIT]?.forEach {
                if (debitDayAmountMap[it.date] != null) {
                    debitDayAmountMap[it.date] = debitDayAmountMap[it.date]!!.plus(it.amount)
                }
            }

            // filling credit amount in day amount map
            txnsByType[TransactionType.CREDIT]?.forEach {
                if (creditDayAmountMap[it.date] != null) {
                    creditDayAmountMap[it.date] = creditDayAmountMap[it.date]!!.plus(it.amount)
                }
            }

            if (debitDayAmountMap.isNotEmpty()) {

                return Graph.Item(
                        Graph(
                                "",
                                debitDayAmountMap.values.map { it.toFloat() },
                                debitDayAmountMap.keys.map{ "" }
                        )
                )

            } else if (creditDayAmountMap.isNotEmpty()) {

                return Graph.Item(
                        Graph(
                                "",
                                creditDayAmountMap.values.map { it.toFloat() },
                                creditDayAmountMap.keys.map{ "" }
                        )
                )

            }

            return null
        }

        private fun List<Transaction>.getTotalListItem(): ListItem<*, *>? {

            // if transactions are for all time, don't show graph
            if (curTransactionTimeRange == TransactionTimeRange.ALL_TIME) {
                return null
            }

            // if both debit and credit transactions are to be shown
            // don't show the graph
            if (transactionTypes.size == 2) {
                return null
            }

            val txnAmountByType =
                    this
                            .groupBy { it.type }
                            .mapValues { pair -> pair.value.sumByDouble { it.amount } }

            txnAmountByType[TransactionType.DEBIT]?.let {
                return Total.Item(
                        Total(
                                "Total",
                                it
                        )
                )
            }

            txnAmountByType[TransactionType.CREDIT]?.let {
                return Total.Item(
                        Total(
                                "Total",
                                it
                        )
                )
            }

            return null
        }

        private fun List<Transaction>.getListItems(): List<ListItem<*, *>> {

            if(this.isEmpty()) {
                return mutableListOf(EmptyListItem("O_O"))
            }

            val list = mutableListOf<ListItem<*, *>>()

            val inputSorted =
                    this
                            .sortedBy { Util.toTimeMillisec(it.date, it.time) }
                            .reversed()

            var date = ""
            var i = 0
            while (i < inputSorted.size) {
                if (date != inputSorted[i].date) {
                    date = inputSorted[i].date
                    list.add(DateHeaderListItem(Util.beautifyDate(date).toUpperCase()))
                }
                list.add(TransactionListItem(inputSorted[i]).setOnClickListener(transactionOnClickListener))
                i += 1
            }

            return list
        }

        override fun onTransactionEdited() {
            fetchTransactions()
        }

        override fun onRefreshClicked() {
            fetchTransactions()
        }

        override fun onAllTimeChecked() {
            curTransactionTimeRange = TransactionTimeRange.ALL_TIME
            fetchTransactions()
        }

        override fun onThisMonthChecked() {
            curTransactionTimeRange = TransactionTimeRange.THIS_MONTH
            fetchTransactions()
        }

        override fun onThisWeekChecked() {
            curTransactionTimeRange = TransactionTimeRange.THIS_WEEK
            fetchTransactions()
        }
    }
}