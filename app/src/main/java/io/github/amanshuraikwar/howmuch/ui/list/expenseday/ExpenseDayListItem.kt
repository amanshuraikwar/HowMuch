package io.github.amanshuraikwar.howmuch.ui.list.expenseday

import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.ui.list.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

/**
 * Created by amanshuraikwar on 11/03/18.
 */
class ExpenseDayListItem(
        private val dayExpense: DayExpense,
        private val dailyLimit: Int)
    : ListItem<ExpenseDayListItemOnClickListener>() {

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getDayExpense() = dayExpense
    fun getDailyLimit() = dailyLimit
}