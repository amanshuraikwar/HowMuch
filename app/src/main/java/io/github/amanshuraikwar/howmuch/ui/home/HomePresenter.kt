package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class HomePresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<HomeContract.View>(appBus, dataManager), HomeContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)

    companion object {
        val ACTION_ADD_EXPENSE = "io.github.amanshuraikwar.howmuch.ui.home.addexpense"
    }

    private var actionServed = false

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        checkDailyAmount()

        if (wasViewRecreated) {

            if (!actionServed) {
                serveAction(getView()!!.getIntentAction())
                actionServed = true
            }

            getDayExpenses()

            getAppBus()
                    .onTransactionsChanged
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                getDayExpenses()
                            },
                            {
                                // todo something on error
                            }
                    ).addToCleanup()

            getAppBus()
                    .onSharedPrefsChanged
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                onSharedPrefsChanged()
                            },
                            {
                                // todo something on error
                            }
                    ).addToCleanup()
        }
    }

    private fun serveAction(action: String) {

        if (action == ACTION_ADD_EXPENSE) {
            getView()?.startAddTransactionActivity()
        }
    }

    private fun checkDailyAmount() {
        getDataManager()
                .getDailyLimitAmount()
                .filter({ it == 0 })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()?.startIntroActivity()
                        },
                        {
                            // todo something on error
                        }
                ).addToCleanup()

    }

    private fun getDayExpenses() {

        getDataManager()
                .getDailyLimitAmount()
                .zipWith(
                        getDataManager()
                                .getExpenseGroupedByDate()
                                .map {
                                    items -> items.reversed()
                                },
                        BiFunction {
                            dailyLimitAmount: Int, dayExpenses: List<DayExpense> ->
                            Pair(dailyLimitAmount, dayExpenses)
                        })
                .zipWith(
                        getDataManager()
                                .getDayExpenseByDate(Util.getCurDateTime()),
                        BiFunction {
                            pair: Pair<Int, List<DayExpense>>, todaysExpense: DayExpense ->
                            Pair(pair, todaysExpense)
                        })
                .map {
                    pair ->
                    getView()
                            ?.getExpenseDayListItems(
                                    pair.first.second,
                                    pair.first.first,
                                    pair.second)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            listItems ->
                            when {
                                listItems != null -> getView()?.displayDayExpenses(listItems)
                            }
                        })
                .addToCleanup()

    }

    override fun onMoreStatsBtnClick() {
        getView()?.startStatsActivity()
    }

    override fun onExpenseDayClick(dayExpense: DayExpense) {
        getView()?.startExpenseDayActivity(dayExpense)
    }

    override fun onAddBtnClick() {
        getView()?.startAddTransactionActivity()
    }

    override fun onSettingBtnClick() {
        getView()?.startSettingsActivity()
    }

    override fun onSharedPrefsChanged() {
        getDayExpenses()
    }
}