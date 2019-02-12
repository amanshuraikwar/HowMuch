package io.github.amanshuraikwar.howmuch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(val id: String,
                       val date: String,
                       val time: String,
                       val currency: String,
                       var amount: Double,
                       val title: String,
                       val description: String,
                       val category: String,
                       val type: TransactionType,
                       val cellRange: String): Parcelable

enum class TransactionType {
    CREDIT, DEBIT
}