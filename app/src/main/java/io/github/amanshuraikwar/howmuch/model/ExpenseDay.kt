package io.github.amanshuraikwar.howmuch.model

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */
data class DayExpense(val date: OffsetDateTime, val amount: Int, val transactionNum: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readSerializable() as OffsetDateTime,
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(date)
        parcel.writeInt(amount)
        parcel.writeInt(transactionNum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayExpense> {
        override fun createFromParcel(parcel: Parcel): DayExpense {
            return DayExpense(parcel)
        }

        override fun newArray(size: Int): Array<DayExpense?> {
            return arrayOfNulls(size)
        }
    }
}