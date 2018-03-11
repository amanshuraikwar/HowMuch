package io.github.amanshuraikwar.howmuch.data.local.room.transaction

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */

@Entity(tableName = "trans")
class Transaction @Ignore constructor(
        private var dateAdded: OffsetDateTime,
        private var amount: Int,
        private var description: String) {

    @PrimaryKey(autoGenerate = true) private var id: Int? = null

    constructor(id: Int, dateAdded: OffsetDateTime, amount: Int, description: String)
            : this(dateAdded, amount, description) {
        this.id = id
    }

    fun getId() = id
    fun getDateAdded() = dateAdded
    fun getAmount() = amount
    fun getDescription() = description
}