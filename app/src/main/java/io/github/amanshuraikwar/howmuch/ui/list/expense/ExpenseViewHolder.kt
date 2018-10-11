package io.github.amanshuraikwar.howmuch.ui.list.expense

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.view.View
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.item_expense.view.*

class ExpenseViewHolder(itemView: View) : ViewHolder<ExpenseListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_expense
    }

    override fun bind(listItem: ExpenseListItem, host: FragmentActivity) {
        itemView.dateTv.text = listItem.expense.date
        itemView.timeTv.text = listItem.expense.time
        itemView.amountTv.text = listItem.expense.amount
        itemView.descriptionTv.text = listItem.expense.description
        itemView.categoryTv.text = listItem.expense.category
    }
}