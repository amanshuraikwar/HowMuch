package io.github.amanshuraikwar.howmuch.ui.list.stats

import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

class StatsListItem(val stats: Stats)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = stats.id

    override fun concreteClass() = StatsListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}