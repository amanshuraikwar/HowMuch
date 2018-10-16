package io.github.amanshuraikwar.howmuch.model

import android.os.Parcel
import android.os.Parcelable

data class Expense(val id: String,
                   val date: String,
                   val time: String,
                   var amount: String,
                   val description: String,
                   val category: String,
                   val cellRange: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(amount)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(cellRange)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }
}