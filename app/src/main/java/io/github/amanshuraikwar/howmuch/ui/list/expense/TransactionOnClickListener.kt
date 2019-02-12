package io.github.amanshuraikwar.howmuch.ui.list.expense

import io.github.amanshuraikwar.howmuch.model.Transaction

interface TransactionOnClickListener {
    fun onClick(transaction: Transaction)
}