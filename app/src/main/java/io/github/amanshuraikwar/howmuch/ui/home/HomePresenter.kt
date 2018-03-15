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

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        checkDailyAmount()

        if (wasViewRecreated) {

            getTodaysExpense()
            getDayExpenses()

            getAppBus()
                    .onTransactionAdded
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                getTodaysExpense()
                                getDayExpenses()
                            },
                            {
                                // todo something on error
                            }
                    ).addToCleanup()
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

    private fun getTodaysExpense() {

        getDataManager()
                .getDayExpenseByDate(Util.getCurDateTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()
                                    ?.displayTodaysExpense(
                                            Util.formatAmount(it.amount))
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
                        getDataManager().getExpenseGroupedByDate().map { items -> items.reversed() },
                        BiFunction { t1: Int, t2: List<DayExpense> ->  Pair(t1, t2)})
                .map { pair -> getView()?.getExpenseDayListItems(pair.second, pair.first) }
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
        getView()?.showAddTransactionDialog()
    }

    override fun onSettingBtnClick() {
        getView()?.startSettingsActivity()
    }
}