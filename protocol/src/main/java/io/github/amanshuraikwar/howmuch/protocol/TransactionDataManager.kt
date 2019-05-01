package io.github.amanshuraikwar.howmuch.protocol

import io.reactivex.Observable

interface TransactionDataManager {

    fun getAllTransactions(): Observable<Iterable<Transaction>>

    fun getTransactionById(id: String): Observable<Transaction>

    fun addTransaction(transaction: Transaction): Observable<Transaction>

    fun updateTransaction(transaction: Transaction): Observable<Transaction>

    fun deleteTransaction(transaction: Transaction): Observable<Transaction>

}