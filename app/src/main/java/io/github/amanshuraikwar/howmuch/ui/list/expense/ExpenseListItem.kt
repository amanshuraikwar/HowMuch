package io.github.amanshuraikwar.howmuch.ui.list.expense

import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

@Suppress("MemberVisibilityCanBePrivate")
class ExpenseListItem(val expense: Expense)
    : ListItem<ExpenseOnClickListener, ListItemTypeFactory>() {

    override fun id() = expense.id

    override fun concreteClass() = ExpenseListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}