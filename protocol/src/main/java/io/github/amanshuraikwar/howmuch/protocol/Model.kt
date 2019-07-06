package io.github.amanshuraikwar.howmuch.protocol

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import kotlinx.android.parcel.Parcelize

enum class TransactionType {
    CREDIT, DEBIT
}

/**
 * To convert any Double into valid money Double
 */
fun Double.money(): Double = "%.2f".format(this).toDouble()

@Parcelize
data class Transaction(val id: String,
                       var date: String,
                       var time: String,
                       var amount: Double,
                       var title: String,
                       var description: String? = null,
                       var categoryId: String,
                       var type: TransactionType,
                       val walletId: String) : Parcelable {

    init {
        amount = amount.money()
    }
}

@Parcelize
data class Category(val id: String,
                    var name: String,
                    var type: TransactionType,
                    var active: Boolean,
                    val monthlyLimit: Double) : Parcelable

@Parcelize
data class Wallet(val id: String,
                  var name: String,
                  var balance: Double,
                  var active: Boolean) : Parcelable {

    init {
        balance = balance.money()
    }
}

@Parcelize
data class User(val id: String,
                var name: String,
                var email: String,
                val userPicUrl: String?) : Parcelable


