package io.github.amanshuraikwar.howmuch.ui.categories

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderListItem
import io.github.amanshuraikwar.howmuch.ui.list.items.StatCategory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CategoriesContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
        fun startHistoryActivity(filter: String? = null)
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchItems()
            }
        }

        private fun fetchItems() {

            getDataManager()
                    .getAllTransactions()
                    .map {
                        it.toList()
                    }
                    .flatMap {
                        txnList ->
                        getDataManager()
                                .getAllCategories()
                                .map {
                                    categoriesList ->
                                    Pair(txnList, categoriesList.toList())
                                }
                    }
                    .flatMap {
                        pair ->
                        Observable
                                .fromCallable {
                                    mutableListOf<ListItem<*, *>>()
                                }
                                .map {
                                    prevList ->
                                    prevList.add(
                                            HeaderListItem("Credit Categories")
                                    )
                                    prevList
                                }
                                .map {
                                    prevList ->
                                    prevList.addAll(
                                            pair.first.getListItems(
                                                    TransactionType.CREDIT,
                                                    pair.second
                                            )
                                    )
                                    prevList
                                }
                                .map {
                                    prevList ->
                                    prevList.add(
                                            HeaderListItem("Debit Categories")
                                    )
                                    prevList
                                }
                                .map {
                                    prevList ->
                                    prevList.addAll(
                                            pair.first.getListItems(
                                                    TransactionType.DEBIT,
                                                    pair.second
                                            )
                                    )
                                    prevList
                                }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching items...")
                            clearSyncError()
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
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()
        }

        fun Double.money(): Double = "%.2f".format(this).toDouble()

        @Suppress("MoveLambdaOutsideParentheses")
        private fun List<Transaction>.getListItems(transactionType: TransactionType,
                                                   categories: List<Category>)
                : List<ListItem<*, *>> {

            val categoryAmountMap = {
                        val categoryIdTxnListMap = this.groupBy { it.categoryId }
                        categories
                                .filter { it.type == transactionType }
                                .groupBy { it }
                                .mapValues {
                                    entry ->
                                    (categoryIdTxnListMap[entry.key.id]?.sumByDouble { it.amount } ?: 0.0).money()
                                }
                    }.invoke()

            return categoryAmountMap.map {
                entry ->
                StatCategory.Item(
                        StatCategory(
                                entry.key,
                                entry.value,
                                {
                                    getView()?.startHistoryActivity("category_id=${it.id}")
                                }
                        )
                )
            }
        }

        override fun onRefreshClicked() {
            fetchItems()
        }

        override fun onRetryClicked() {
            fetchItems()
        }
    }
}