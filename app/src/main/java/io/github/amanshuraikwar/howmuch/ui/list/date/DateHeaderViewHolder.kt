package io.github.amanshuraikwar.howmuch.ui.list.date

import androidx.annotation.LayoutRes
import android.view.View
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import kotlinx.android.synthetic.main.item_date_header.view.*

class DateHeaderViewHolder(itemView: View) : ViewHolder<DateHeaderListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_date_header
    }

    override fun bind(listItemDate: DateHeaderListItem,
                      host: FragmentActivity) {
        itemView.dateTv.text = listItemDate.date
        if (listItemDate.first) {
            itemView.topLine.visibility = View.GONE
        } else {
            itemView.topLine.visibility = View.VISIBLE
        }
    }
}