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
        itemView.timeTv.text = Util.beautifyTime(transaction.time)

        itemView.circleCv.setCardBackgroundColor(ContextCompat.getColor(host, listItem.color1))
        itemView.topLine.setBackgroundColor(ContextCompat.getColor(host, listItem.color2))
        itemView.bottomLine.setBackgroundColor(ContextCompat.getColor(host, listItem.color2))

        if (listItem.last) {
            itemView.bottomLine.visibility = View.GONE
        } else {
            itemView.bottomLine.visibility = View.VISIBLE
        }

        itemView.parentCl.setOnClickListener {
            listItem.onClickListener.onClick(transaction)
        }
    }
}