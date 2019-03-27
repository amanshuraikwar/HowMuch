package io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary

import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.item_expense_category_summary.view.*

class ExpenseCategorySummaryViewHolder(itemView: View) : ViewHolder<ExpenseCategorySummaryListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_expense_category_summary
    }

    override fun bind(listItem: ExpenseCategorySummaryListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.titleTv.text = listItem.expenseCategorySummary.name
        itemView.amountTv.text = "-${listItem.expenseCategorySummary.actual}"
        itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.red))
    }
}