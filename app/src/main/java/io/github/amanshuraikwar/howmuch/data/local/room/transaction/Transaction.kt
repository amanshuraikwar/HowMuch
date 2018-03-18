package io.github.amanshuraikwar.howmuch.data.local.room.transaction

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */

@Entity(tableName = "trans")
class Transaction @Ignore constructor(
        private var dateAdded: OffsetDateTime,
        private var amount: Int,
        private var description: String) : Parcelable {

    @PrimaryKey(autoGenerate = true) private var id: Int? = null

    constructor(parcel: Parcel) : this(
            parcel.readSerializable() as OffsetDateTime,
            parcel.readInt(),
            parcel.readString()) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    constructor(id: Int?, dateAdded: OffsetDateTime, amount: Int, description: String)
            : this(dateAdded, amount, description) {
        this.id = id
    }

    fun getId() = id
    fun getDateAdded() = dateAdded
    fun getAmount() = amount
    fun getDescription() = description

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(dateAdded)
        parcel.writeInt(amount)
        parcel.writeString(description)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}