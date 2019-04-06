package io.github.amanshuraikwar.howmuch.ui.list.empty

import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

class EmptyListItem(val message: String)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = message

    override fun concreteClass() = EmptyListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}