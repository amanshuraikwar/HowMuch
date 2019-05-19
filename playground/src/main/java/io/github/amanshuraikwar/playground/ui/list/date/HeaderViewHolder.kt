package io.github.amanshuraikwar.playground.ui.list.date

import androidx.annotation.LayoutRes
import android.view.View
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.playground.R
import kotlinx.android.synthetic.main.item_header.view.*

class HeaderViewHolder(itemView: View) : ViewHolder<HeaderListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_header
    }

    override fun bind(listItem: HeaderListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.dateTv.text = listItem.date
    }
}