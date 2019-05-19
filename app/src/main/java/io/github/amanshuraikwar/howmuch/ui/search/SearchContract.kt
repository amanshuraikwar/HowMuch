package io.github.amanshuraikwar.howmuch.ui.search

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SearchContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun startTransactionActivity(transaction: Transaction)
    }

    interface Presenter : BasePresenter<View> {
        fun search(query: String)
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var transactions: List<Transaction>
        private lateinit var categories: List<Category>
        private lateinit var wallets: List<Wallet>

        private val transactionOnClickListener=
                object : TransactionOnClickListener {
                    override fun onClick(transaction: Transaction) {
                        getView()?.startTransactionActivity(transaction)
                    }
                }

        override fun search(query: String) {

            if (query.isEmpty()) {
                return
            }

            Observable
                    .fromCallable { query }
                    .flatMap {
                        q ->
                        if (!::transactions.isInitialized) {
                            getDataManager()
                                    .getAllTransactions()
                                    .map {
                                        transactions = it.toList()
                                    }
                                    .map { q }
                        } else {
                            Observable.fromCallable { q }
                        }
                    }
                    .flatMap {
                        Observable.fromCallable {
                            transactions.filter {
                                it.title.contains(query, true)
                            }
                        }
                    }
                    .map {
                        transactions ->
                        if (transactions.isEmpty()) {
                            listOf<ListItem<*, *>>(EmptyListItem(""))
                        } else {
                            transactions.map {
                                transaction ->
                                TransactionListItem(transaction)
                                        .setOnClickListener(transactionOnClickListener)
                            }
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Searching transactions...")
                        }
                    }
                    .subscribe(
                            {

                                Log.d(tag, "fetchItems: Items: $it ")

                                getView()?.run {
                                    submitList(it)
                                    hideLoading()
                                }
                            },
                            {

                                it.printStackTrace()

                                Log.e(tag, "fetchItems: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                }
                            }
                    )
                    .addToCleanup()

        }
    }
}