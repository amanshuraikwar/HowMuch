package io.github.amanshuraikwar.howmuch.ui.list.transaction

import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

class TransactionListItem(val transaction: Transaction)
    : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

    override fun id() = transaction.id

    override fun concreteClass() = TransactionListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}