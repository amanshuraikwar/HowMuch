package io.github.amanshuraikwar.playground.ui.list.date

import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.playground.ui.ListItemTypeFactory

class HeaderListItem(val date: String)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = date

    override fun concreteClass() = HeaderListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}