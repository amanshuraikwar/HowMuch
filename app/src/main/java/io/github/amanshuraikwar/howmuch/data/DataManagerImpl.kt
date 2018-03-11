package io.github.amanshuraikwar.howmuch.data

import android.content.Context
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.data.model.DayExpense
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class DataManagerImpl @Inject constructor(@ApplicationContext val context: Context) : DataManager {

    @Inject
    lateinit var localDataManager: LocalDataManager

    override fun getAllTransactions() = localDataManager.getAllTransactions()

    override fun getAllTransactionsForDate(date: OffsetDateTime)
            = localDataManager.getAllTransactionsForDate(date)

    override fun getExpenseGroupedByDate() = localDataManager.getExpenseGroupedByDate()

    override fun addTransaction(transaction: Transaction)
            = localDataManager.addTransaction(transaction)

    override fun deleteTransaction(transaction: Transaction)
            = localDataManager.deleteTransaction(transaction)

    override fun updateTransaction(transaction: Transaction)
            = localDataManager.updateTransaction(transaction)

    override fun getDailyLimitAmount() = localDataManager.getDailyLimitAmount()

    override fun setDailyLimitAmount(amount: Int) = localDataManager.setDailyLimitAmount(amount)

    override fun getDayExpenseByDate(date: OffsetDateTime) = localDataManager.getDayExpenseByDate(date)
}