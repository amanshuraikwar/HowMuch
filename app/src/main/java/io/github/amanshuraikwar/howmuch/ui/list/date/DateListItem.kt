package io.github.amanshuraikwar.howmuch.ui.list.date

import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

@Suppress("MemberVisibilityCanBePrivate")
class DateListItem(val date: String)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = date

    override fun concreteClass() = DateListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}