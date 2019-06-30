package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_divider.view.*

class DividerPadded {

    class Item
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = this.hashCode().toString()

        @Suppress("HasPlatformType")
        override fun concreteClass() = DividerPadded::class.java.name

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_divider_padded
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            // do nothing
        }
    }
}