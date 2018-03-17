package io.github.amanshuraikwar.howmuch.ui.list.verybigexpense

import io.github.amanshuraikwar.howmuch.ui.list.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.SimpleListItemOnClickListener

/**
 * Created by amanshuraikwar on 11/03/18.
 */
class VeryBigExpenseListItem(
        val expenseHeading: String,
        val expenseAmount: Int,
        val primaryColor: Int,
        val secondaryColor: Int,
        val backgroundColor: Int)
    : ListItem<SimpleListItemOnClickListener>() {

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}