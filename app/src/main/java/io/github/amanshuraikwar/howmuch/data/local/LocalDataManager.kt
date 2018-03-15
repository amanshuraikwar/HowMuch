package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.reactivex.Observable
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */
interface LocalDataManager {

    fun getAllTransactions(): Observable<List<Transaction>>
    fun getAllTransactionsForDate(date: OffsetDateTime): Observable<List<Transaction>>
    fun getDayExpenseByDate(date: OffsetDateTime): Observable<DayExpense>
    fun getExpenseGroupedByDate(): Observable<List<DayExpense>>
    fun addTransaction(transaction: Transaction): Observable<Boolean>
    fun deleteTransaction(transaction: Transaction): Observable<Boolean>
    fun updateTransaction(transaction: Transaction): Observable<Boolean>
    fun getDailyLimitAmount(): Observable<Int>
    fun setDailyLimitAmount(amount: Int): Observable<Boolean>
}