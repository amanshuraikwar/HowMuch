package io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary

import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.model.ExpenseCategorySummary
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

@Suppress("MemberVisibilityCanBePrivate")
class ExpenseCategorySummaryListItem(val expenseCategorySummary: ExpenseCategorySummary)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = expenseCategorySummary.id

    override fun concreteClass() = ExpenseCategorySummaryListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}