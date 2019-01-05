package io.github.amanshuraikwar.howmuch.ui.list.date

import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
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

    override fun bind(listItem: DateListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.dateTv.text = listItem.date
    }
}