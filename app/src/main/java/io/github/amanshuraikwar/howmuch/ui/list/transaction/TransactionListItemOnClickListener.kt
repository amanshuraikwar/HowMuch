package io.github.amanshuraikwar.howmuch.ui.list.transaction

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.model.DayExpense

/**
 * Created by amanshuraikwar on 13/03/18.
 */
interface TransactionListItemOnClickListener {
    fun onEditClick(transaction: Transaction)
    fun onDeleteClick(transaction: Transaction)
}