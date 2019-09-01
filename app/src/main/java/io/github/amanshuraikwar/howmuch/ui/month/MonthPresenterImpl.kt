package io.github.amanshuraikwar.howmuch.ui.month

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class MonthPresenterImpl<V : MonthView> constructor(appBus: AppBus,
                                                             dataManager: DataManager)
    : HowMuchBasePresenterImpl<V>(appBus, dataManager), MonthPresenter<V> {

    private val tag = Util.getTag(MonthPresenterImpl::class.java)

    protected var curMonth = 0
    protected var curYear = 0

    protected var displayedMonth = 0
    protected var displayedYear = 0

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)
        if (wasViewRecreated) {
            curMonth = Util.getCurMonthNumber()
            curYear = Util.getCurYearNumber()
            displayedMonth = curMonth
            displayedYear = curYear
            getView()?.getInitDisplayedMonth()?.let {
                displayedMonth = it
            }
            getView()?.getInitDisplayedYear()?.let {
                displayedYear = it
            }
            fetchItems()
        }
    }

    protected abstract fun getListItems(txnList: List<Transaction>)
            : Observable<List<ListItem<*,*>>>

    protected fun fetchItems() {

        getDataManager()
                .getAllTransactions()
                .map {
                    it.toList()
                            .filter { txn -> txn.type == TransactionType.DEBIT }
                            .filterByDate(
                                    month = displayedMonth,
                                    year = displayedYear
                            )
                }
                .flatMap { getListItems(it) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    getView()?.run{
                        showLoading("Fetching items...")
                        clearSyncError()
                        updateMonth(
                                true,
                                "${Util.getMonthName(displayedMonth)} $displayedYear",
                                !(displayedMonth == curMonth && displayedYear == curYear)
                        )
                    }
                }
                .subscribe(
                        {
                            getView()?.run {
                                submitList(it)
                                hideLoading()
                            }
                        },
                        {
                            Log.e(tag, "fetchItems: Error: ${it.message}", it)
                            getView()?.run {
                                showError(it.message ?: "Please try again!")
                                hideLoading()
                                setSyncError()
                            }
                        }
                )
                .addToCleanup()
    }

    override fun onPreviousMonthClicked() {
        if (displayedMonth == 1) {
            displayedMonth = 12
            displayedYear--
        } else {
            displayedMonth--
        }
        fetchItems()
    }

    override fun onNextMonthClicked() {
        if (displayedMonth == 12) {
            displayedMonth = 1
            displayedYear++
        } else {
            displayedMonth++
        }
        fetchItems()
    }

    override fun onRetryClicked() {
        fetchItems()
    }

    override fun onRefreshClicked() {
        fetchItems()
    }

    private fun List<Transaction>.filterByDate(month: Int,
                                               year: Int)
            : List<Transaction> {
        return this.filter {
            val parts = it.date.split("-")
            parts[1].toInt() == month && parts[2].toInt() == year
        }
    }

}