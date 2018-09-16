package io.github.amanshuraikwar.howmuch.ui.stats

import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.model.ExpenseCategorySummary
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.expense.ExpenseListItem
import io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary.ExpenseCategorySummaryListItem
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StatsPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<StatsContract.View>(appBus, dataManager), StatsContract.Presenter {

    private val TAG = Util.getTag(this)

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            getHistory()
        }
    }

    private fun getHistory() {

        getDataManager().getAuthenticationManager().let {
            authMan ->
            Log.d(TAG, "onAttach:checking permissions")
            if (authMan.hasPermissions()) {
                Log.d(TAG, "onAttach:has permissions")
                if (authMan.getLastSignedAccount()?.account != null) {

                    Log.d(TAG, "onAttach:account is not null")

                    val observable = getDataManager()
                            .readSpreadSheet(
                                    "1HzV18zWmo_-LwuHT_WtXHvR18f7GwQj2EKNKq47iOIM"
                                    ,"Summary!B22:E",
                                    getView()!!
                                            .getGoogleAccountCredential(
                                                    authMan.getLastSignedAccount()!!.account!!))

                    Log.d(TAG, "onAttach: $observable")

                    disposables.add(observable
                            .map { getExpenseCategorySummaryList(it) }
                            .map { getListItems(it) }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        Log.d(TAG, "reading spread sheets: onNext")
                                        getView()?.submitList(it)
                                        getView()?.hideLoadingOverlay()
                                    },
                                    {
                                        Log.d(TAG, "reading spread sheets: error thrown")
                                        getView()?.showErrorOverlay()
                                    },
                                    {
                                        Log.d(TAG, "reading spread sheets: completed")
                                    },
                                    {
                                        getView()?.showLoadingOverlay()
                                        Log.d(TAG, "reading spread sheets: subscribed")
                                    }))
                }
            }
        }
    }

    private fun getExpenseCategorySummaryList(input: MutableList<MutableList<Any>>): List<ExpenseCategorySummary> {
        val list = mutableListOf<ExpenseCategorySummary>()
        var count = 0
        input.forEach {
            if (it.size == 4) {
                list.add(ExpenseCategorySummary(count.toString(), it[0].toString() + it[1].toString(), it[2].toString(), it[3].toString()))
            } else {
                list.add(ExpenseCategorySummary(count.toString(), it[0].toString(), it[1].toString(), it[2].toString()))
            }
            count++
        }
        return list
    }

    private fun getListItems(input: List<ExpenseCategorySummary>): List<ListItem<*, *>> {
        val list = mutableListOf<ListItem<*, *>>()
        input.forEach{
            list.add(ExpenseCategorySummaryListItem(it))
        }
        return list
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: called")
    }

    override fun onLoadingRetryClicked() {
        getHistory()
    }
}