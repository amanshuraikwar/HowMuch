package io.github.amanshuraikwar.howmuch.ui.list.stats

import androidx.annotation.LayoutRes
import android.view.View
import androidx.core.content.ContextCompat
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.util.Util;
import kotlinx.android.synthetic.main.item_expense_category_summary.view.*

class StatsViewHolder(itemView: View) : ViewHolder<StatsListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_expense_category_summary
    }

    override fun bind(listItem: StatsListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.titleTv.text = listItem.stats.name
        itemView.amountTv.text = "-${listItem.stats.actual}"
        itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.red))
    }
}