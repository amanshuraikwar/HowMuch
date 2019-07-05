package io.github.amanshuraikwar.howmuch.ui.list.transaction

import android.graphics.Color
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

class TransactionListItem(val transaction: Transaction,
                          val showDate: Boolean = false,
                          val color1: Int = R.color.gray3,
                          val color2: Int = R.color.gray1,
                          val last: Boolean = false)
    : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

    override fun id() = transaction.id

    override fun concreteClass() = TransactionListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}