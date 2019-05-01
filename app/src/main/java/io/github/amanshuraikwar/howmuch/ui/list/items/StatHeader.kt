package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_header.view.*

class StatHeader(val title: String) {

    class Item(val statHeader: StatHeader)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = statHeader.title

        override fun concreteClass() = statHeader::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_header
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.titleTv.text = listItem.statHeader.title
        }
    }
}