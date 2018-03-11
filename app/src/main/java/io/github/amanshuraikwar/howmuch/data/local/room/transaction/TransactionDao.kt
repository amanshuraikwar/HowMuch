package io.github.amanshuraikwar.howmuch.data.local.room.transaction

import android.arch.persistence.room.*
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */
@Dao
interface TransactionDao {

    @Query("SELECT * FROM trans ORDER BY datetime(dateAdded)")
    fun getAll(): List<Transaction>

    @Query("SELECT * FROM trans where date(dateAdded) = date(:datetime)")
    fun getAllForDate(datetime: OffsetDateTime): List<Transaction>

    @Insert fun insert(transaction: Transaction)
    @Delete fun delete(transaction: Transaction)
    @Update fun update(transaction: Transaction)
}