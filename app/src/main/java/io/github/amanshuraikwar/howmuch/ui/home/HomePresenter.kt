package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.NumberUtil
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class HomePresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<HomeContract.View>(appBus, dataManager), HomeContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)

    private var disposables: Set<Disposable> = mutableSetOf()

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        checkDailyAmount()

        if (wasViewRecreated) {
            getTodaysExpense()
            getDayExpenses()

            disposables.plusElement(
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
                            )
            )
        }
    }

    override fun onDetach() {
        super.onDetach()

        for (disposable in disposables) {
            if (! disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    private fun checkDailyAmount() {
        disposables.plusElement(
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
                        )
        )
    }

    private fun getTodaysExpense() {

        disposables.plusElement(
                getDataManager()
                        .getDayExpenseByDate(NumberUtil.getCurDateTime())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    getView()
                                            ?.displayTodaysExpense(
                                                    NumberUtil.formatAmount(it.amount))
                                },
                                {
                                    // todo something on error
                                }
                        )
        )
    }

    private fun getDayExpenses() {
        disposables.plusElement(
                getDataManager()
                        .getDailyLimitAmount()
                        .zipWith(
                                getDataManager().getExpenseGroupedByDate(),
                                BiFunction { t1: Int, t2: List<DayExpense> ->  Pair(t1, t2)})
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    getView()?.displayDayExpenses(it.second.reversed(), it.first)
                                },
                                {
                                    // todo something on error
                                }
                        )
        )
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