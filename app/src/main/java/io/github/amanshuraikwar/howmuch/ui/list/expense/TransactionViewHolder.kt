package io.github.amanshuraikwar.howmuch.ui.list.expense

import androidx.annotation.LayoutRes
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.model.TransactionType
import io.github.amanshuraikwar.howmuch.util.Util
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
            itemView.amountTv.text = "-${transaction.currency}${transaction.amount}"
        } else {
            itemView.currencyTv.text = ""
            itemView.currencyTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            itemView.amountTv.text = "+${transaction.currency}${transaction.amount}"
        }

        itemView.timeTv.text = Util.beautifyTime(transaction.time)

        itemView.parentLl.setOnClickListener {
            listItem.onClickListener.onClick(transaction)
        }
    }
}