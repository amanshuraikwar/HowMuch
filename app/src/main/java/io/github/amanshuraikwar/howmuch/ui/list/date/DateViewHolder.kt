package io.github.amanshuraikwar.howmuch.ui.list.date

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.view.View
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.item_expense.view.*

class DateViewHolder(itemView: View) : ViewHolder<DateListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_date
    }

    override fun bind(listItem: DateListItem, host: FragmentActivity) {
        itemView.dateTv.text = listItem.date
    }
}