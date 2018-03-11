package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.data.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * Created by amanshuraikwar on 07/03/18.
 */
interface HomeContract {

    interface View : BaseView {
        fun startIntroActivity()
        fun startExpenseDayActivity(dayExpense: DayExpense)
        fun startStatsActivity()
        fun showAddTransactionDialog()
        fun startSettingsActivity()
        fun displayTodaysExpense(amount: String)
        fun displayDayExpenses(dayExpenses: List<DayExpense>, dailyLimit: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun onMoreStatsBtnClick()
        fun onExpenseDayClick(dayExpense: DayExpense)
        fun onAddBtnClick()
        fun onSettingBtnClick()
    }
}