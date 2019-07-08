package io.github.amanshuraikwar.howmuch.ui.list.transaction

import androidx.annotation.LayoutRes
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.util.Util;
import kotlinx.android.synthetic.main.item_category_transaction.view.*

class TransactionViewHolder(itemView: View) : ViewHolder<TransactionListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_category_transaction
    }

    override fun bind(listItem: TransactionListItem, host: FragmentActivity) {
        val transaction = listItem.transaction
        itemView.titleTv.text = transaction.title
        itemView.amountTv.text = transaction.amount.toString()
        itemView.timeTv.text = Util.beautifyTime(transaction.time)

        itemView.amountCv.setCardBackgroundColor(ContextCompat.getColor(host, listItem.color1))
        itemView.topLine.setBackgroundColor(ContextCompat.getColor(host, listItem.color2))
        itemView.lineV.setBackgroundColor(ContextCompat.getColor(host, listItem.color2))

        if (listItem.last) {
            itemView.lineV.visibility = View.GONE
        } else {
            itemView.lineV.visibility = View.VISIBLE
        }

        itemView.parentCl.setOnClickListener {
            listItem.onClickListener.onClick(transaction)
        }
    }
}