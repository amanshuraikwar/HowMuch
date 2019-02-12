package io.github.amanshuraikwar.howmuch.ui.list.expense

import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

@Suppress("MemberVisibilityCanBePrivate")
class TransactionListItem(val transaction: Transaction)
    : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

    override fun id() = transaction.id.toString()

    override fun concreteClass() = TransactionListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}