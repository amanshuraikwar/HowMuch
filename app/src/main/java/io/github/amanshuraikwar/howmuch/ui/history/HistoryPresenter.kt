package io.github.amanshuraikwar.howmuch.ui.history

import android.util.Log
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.expense.ExpenseListItem
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<HistoryContract.View>(appBus, dataManager), HistoryContract.Presenter {

    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        getDataManager().getAuthenticationManager().let {
            authMan ->
            Log.d(TAG, "onAttach:checking permissions")
            if (authMan.hasPermissions()) {
                Log.d(TAG, "onAttach:has permissions")
                if (authMan.getLastSignedAccount()?.account != null) {

                    Log.d(TAG, "onAttach:account is not null")

                    getDataManager()
                            .readSpreadSheet(
                                    "1HzV18zWmo_-LwuHT_WtXHvR18f7GwQj2EKNKq47iOIM"
                                    ,"Transactions!B5:E",
                                    getView()!!
                                            .getGoogleAccountCredential(
                                                    authMan.getLastSignedAccount()!!.account!!))
                            .map { getExpenseList(it) }
                            .map { getListItems(it) }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                getView()?.submitList(it)
                            }
                }
            }
        }
    }

    private fun getExpenseList(input: MutableList<MutableList<Any>>): List<Expense> {
        val list = mutableListOf<Expense>()
        var count = 0
        input.forEach {
            list.add(Expense(count.toString(), it[0].toString(), it[1].toString(), it[2].toString(), it[3].toString()))
            count++
        }
        return list
    }

    private fun getListItems(input: List<Expense>): List<ListItem<*, *>> {
        val list = mutableListOf<ListItem<*, *>>()
        input.forEach{
            list.add(ExpenseListItem(it))
        }
        return list
    }

}