package io.github.amanshuraikwar.howmuch.ui.list.date

import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

class DateHeaderListItem(val date: String,
                         val color1: Int = R.color.gray3,
                         val color2: Int = R.color.gray1,
                         val first: Boolean = false)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = date

    override fun concreteClass() = DateHeaderListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}