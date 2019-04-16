package io.github.amanshuraikwar.howmuch.ui.list.transaction

import android.content.res.ColorStateList
import androidx.annotation.LayoutRes
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.base.util.Util;
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionViewHolder(itemView: View) : ViewHolder<TransactionListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_transaction
    }

    override fun bind(listItem: TransactionListItem, host: FragmentActivity) {

        val transaction = listItem.transaction

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

        if (listItem.showDate) {
            itemView.timeTv.text = Util.beautifyDate(transaction.date) + "  " + Util.beautifyTime(transaction.time)
        } else {
            itemView.timeTv.text = Util.beautifyTime(transaction.time)
        }

        itemView.parentLl.setOnClickListener {
            listItem.onClickListener.onClick(transaction)
        }
    }
}