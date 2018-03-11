package io.github.amanshuraikwar.howmuch.data.local

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.github.amanshuraikwar.howmuch.data.local.room.AppDatabase
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.data.local.sharedprefs.SharedPrefsKeys
import io.github.amanshuraikwar.howmuch.data.model.DayExpense
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

    override fun getAllTransactions(): Observable<List<Transaction>> {

        return Observable.create {
            try {
                it.onNext(appDatabase.transactionDao().getAll())
                it.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun getAllTransactionsForDate(date: OffsetDateTime): Observable<List<Transaction>> {

        return Observable.create {
            try {
                it.onNext(appDatabase.transactionDao().getAllForDate(date))
                it.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun getDayExpenseByDate(date: OffsetDateTime): Observable<DayExpense> {

        return Observable.create {

            try {
                val transactions = appDatabase.transactionDao().getAllForDate(date)
                var transactionNum = 0
                var amount = 0

                for (transaction in transactions) {
                    transactionNum += 1
                    amount += transaction.getAmount()
                }

                it.onNext(DayExpense(date.toLocalDate(), amount, transactionNum))
                it.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun getExpenseGroupedByDate(): Observable<List<DayExpense>> {

        return Observable.create {

            try {
                it.onNext(groupTransactions(appDatabase.transactionDao().getAll()))
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    private fun groupTransactions(transactions: List<Transaction>): List<DayExpense> {

        val dayExpenses = mutableListOf<DayExpense>()
        var lastDate: LocalDate? = null
        var transactionNum = 0
        var amount = 0

        for (transaction in transactions) {

            if (lastDate != transaction.getDateAdded().toLocalDate()) {

                if (transactionNum != 0) {
                    if (lastDate != null) {
                        dayExpenses.add(DayExpense(lastDate, amount, transactionNum))
                    }
                }

                lastDate = transaction.getDateAdded().toLocalDate()
                transactionNum = 1
                amount = transaction.getAmount()
            } else {
                amount += transaction.getAmount()
                transactionNum += 1
            }
        }

        if (lastDate != null) {
            dayExpenses.add(DayExpense(lastDate, amount, transactionNum))
        }

        return dayExpenses
    }

    override fun addTransaction(transaction: Transaction): Observable<Boolean> {

        return Observable.create {

            try {
                appDatabase.transactionDao().insert(transaction)
                it.onNext(true)
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun deleteTransaction(transaction: Transaction): Observable<Boolean> {

        return Observable.create {

            try {
                appDatabase.transactionDao().delete(transaction)
                it.onNext(true)
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun updateTransaction(transaction: Transaction): Observable<Boolean> {

        return Observable.create {

            try {
                appDatabase.transactionDao().update(transaction)
                it.onNext(true)
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setDailyLimitAmount(amount: Int): Observable<Boolean> {

        return Observable.create {

            try {
                with (sharedPrefs.edit()) {
                    putInt(SharedPrefsKeys.KEY_DAILY_LIMIT_AMOUNT, amount)
                    commit()
                }
                it.onNext(true)
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }

    override fun getDailyLimitAmount(): Observable<Int> {

        return Observable.create {

            try {
                it.onNext(sharedPrefs.getInt(SharedPrefsKeys.KEY_DAILY_LIMIT_AMOUNT, 0))
            } catch (e: Exception) {
                e.printStackTrace()
                it.tryOnError(e)
            }
        }
    }
}