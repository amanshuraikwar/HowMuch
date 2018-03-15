package io.github.amanshuraikwar.howmuch.data.local

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.github.amanshuraikwar.howmuch.data.local.room.AppDatabase
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.data.local.sharedprefs.SharedPrefsKeys
import io.github.amanshuraikwar.howmuch.model.DayExpense
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.Observable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class LocalDataManagerImpl @Inject constructor() : LocalDataManager {

    val TAG = LogUtil.getLogTag(this)

    @Inject lateinit var appDatabase: AppDatabase

    @Inject lateinit var sharedPrefs: SharedPreferences

    override fun getAllTransactions()
            : Observable<List<Transaction>>
            = Observable.fromCallable({ appDatabase.transactionDao().getAll() })


    override fun getAllTransactionsForDate(date: OffsetDateTime)
            : Observable<List<Transaction>>
            = Observable.fromCallable({ appDatabase.transactionDao().getAllForDate(date) })

    override fun getDayExpenseByDate(date: OffsetDateTime)
            : Observable<DayExpense>
            = Observable
            .fromCallable({ appDatabase.transactionDao().getAllForDate(date) })
            .map { transactions -> convertToDayExpense(transactions, date) }

    private fun convertToDayExpense(transactions: List<Transaction>, date: OffsetDateTime)
            : DayExpense {

        var transactionNum = 0
        var amount = 0

        for (transaction in transactions) {
            transactionNum += 1
            amount += transaction.getAmount()
        }

        return DayExpense(date, amount, transactionNum)
    }

    override fun getExpenseGroupedByDate(): Observable<List<DayExpense>> {

        return Observable
                .fromCallable({ appDatabase.transactionDao().getAll() })
                .map { transactions -> groupTransactions(transactions) }
    }

    private fun groupTransactions(transactions: List<Transaction>): List<DayExpense> {

        val dayExpenses = mutableListOf<DayExpense>()
        var lastDate: OffsetDateTime? = null
        var transactionNum = 0
        var amount = 0

        for (transaction in transactions) {

            when {
                lastDate == null -> {

                    lastDate = transaction.getDateAdded()
                    transactionNum = 1
                    amount = transaction.getAmount()
                }
                lastDate.toLocalDate() != transaction.getDateAdded().toLocalDate() -> {

                    if (transactionNum != 0) {
                        dayExpenses.add(DayExpense(lastDate, amount, transactionNum))
                    }

                    lastDate = transaction.getDateAdded()
                    transactionNum = 1
                    amount = transaction.getAmount()
                }
                else -> {
                    amount += transaction.getAmount()
                    transactionNum += 1
                }
            }
        }

        if (lastDate != null) {
            dayExpenses.add(DayExpense(lastDate, amount, transactionNum))
        }

        return dayExpenses
    }

    override fun addTransaction(transaction: Transaction)
            : Observable<Boolean>
            = Observable.fromCallable(
                {
                    appDatabase.transactionDao().insert(transaction)
                    return@fromCallable true
                })

    override fun deleteTransaction(transaction: Transaction)
            : Observable<Boolean>
            = Observable.fromCallable(
            {
                appDatabase.transactionDao().delete(transaction)
                return@fromCallable true
            })

    override fun updateTransaction(transaction: Transaction)
            : Observable<Boolean>
            = Observable.fromCallable(
            {
                appDatabase.transactionDao().update(transaction)
                return@fromCallable true
            })

    @SuppressLint("CommitPrefEdits")
    override fun setDailyLimitAmount(amount: Int)
            : Observable<Boolean>
            = Observable.fromCallable(
            {
                with (sharedPrefs.edit()) {
                    putInt(SharedPrefsKeys.KEY_DAILY_LIMIT_AMOUNT, amount)
                    commit()
                }
                return@fromCallable true
            })

    override fun getDailyLimitAmount()
            : Observable<Int>
            = Observable.fromCallable({ sharedPrefs.getInt(SharedPrefsKeys.KEY_DAILY_LIMIT_AMOUNT, 0) })
}