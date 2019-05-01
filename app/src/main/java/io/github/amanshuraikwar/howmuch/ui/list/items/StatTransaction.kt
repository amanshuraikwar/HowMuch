package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_header.view.titleTv
import kotlinx.android.synthetic.main.item_stat_transaction.view.*

class StatTransaction(val transaction: Transaction, val showDate: Boolean = false) {

    class Item(val statTransaction: StatTransaction)
        : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

        override fun id() = statTransaction.transaction.id

        override fun concreteClass() = statTransaction::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_transaction
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val transaction = listItem.statTransaction.transaction

            itemView.titleTv.text = transaction.title
            itemView.amountTv.text = transaction.amount.toString()

            if (transaction.type == TransactionType.DEBIT) {

                itemView.currencyTv.text = ""
                itemView.currencyTv.setTextColor(ContextCompat.getColor(host, R.color.red))
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.red))
                itemView.amountTv.text = "-${transaction.amount}"

                itemView.transactionTypeIb.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.red)
                )

                itemView.transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp)

            } else {

                itemView.currencyTv.text = ""
                itemView.currencyTv.setTextColor(ContextCompat.getColor(host, R.color.green))
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.green))
                itemView.amountTv.text = "+${transaction.amount}"

                itemView.transactionTypeIb.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.green)
                )

                itemView.transactionTypeIb.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp)
            }

            if (listItem.statTransaction.showDate) {
                itemView.timeTv.text = Util.beautifyDate(transaction.date) + "  " + Util.beautifyTime(transaction.time)
            } else {
                itemView.timeTv.text = Util.beautifyTime(transaction.time)
            }

            itemView.parentLl.setOnClickListener {
                listItem.onClickListener.onClick(transaction)
            }
        }
    }
}