package io.github.amanshuraikwar.howmuch.ui.list.transaction

import io.github.amanshuraikwar.howmuch.protocol.Transaction

interface TransactionOnClickListener {
    fun onClick(transaction: Transaction)
}