package io.github.amanshuraikwar.howmuch.ui.list.expenseday

import io.github.amanshuraikwar.howmuch.model.DayExpense

/**
 * Created by amanshuraikwar on 13/03/18.
 */
interface ExpenseDayListItemOnClickListener {
    fun onClick(dayExpense: DayExpense)
}