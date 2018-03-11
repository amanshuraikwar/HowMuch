package io.github.amanshuraikwar.howmuch.data.local.room

import android.arch.persistence.room.*
import android.arch.persistence.room.TypeConverters
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.TransactionDao

/**
 * Created by amanshuraikwar on 09/03/18.
 */
@Database(entities = arrayOf(Transaction::class), version = 3, exportSchema = false)
@TypeConverters(io.github.amanshuraikwar.howmuch.data.local.room.TypeConverters :: class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
}