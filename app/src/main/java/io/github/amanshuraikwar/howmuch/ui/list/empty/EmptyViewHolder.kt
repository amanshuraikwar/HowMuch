package io.github.amanshuraikwar.howmuch.ui.list.empty

import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import android.view.View
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.item_empty.view.*

class EmptyViewHolder(itemView: View) : ViewHolder<EmptyListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_empty
    }

    override fun bind(listItem: EmptyListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.messageTv.text = listItem.message
    }
}