package io.github.amanshuraikwar.howmuch.ui.list.transaction

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.ui.list.ListItem
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.SimpleListItemOnClickListener

/**
 * Created by amanshuraikwar on 11/03/18.
 */
class TransactionListItem(
        private val transaction: Transaction,
        private val maxTransactionAmount: Int)
    : ListItem<SimpleListItemOnClickListener>() {

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getTransaction() = transaction
    fun getMaxTransactionAmount() = maxTransactionAmount
}