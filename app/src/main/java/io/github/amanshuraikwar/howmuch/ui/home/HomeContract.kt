package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.ui.list.ListItem

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
        fun displayDayExpenses(listItems: List<ListItem<*>>)
        fun getExpenseDayListItems(dayExpenses: List<DayExpense>,
                                   dailyLimit: Int,
                                   todaysExpense: DayExpense): List<ListItem<*>>
    }

    interface Presenter : BasePresenter<View> {
        fun onMoreStatsBtnClick()
        fun onExpenseDayClick(dayExpense: DayExpense)
        fun onAddBtnClick()
        fun onSettingBtnClick()
        fun onSharedPrefsChanged()
    }
}