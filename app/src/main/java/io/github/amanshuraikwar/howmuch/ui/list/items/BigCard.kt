package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_big_card.view.*

class BigCard(val text: String) {

    class Item(val bigCard: BigCard)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = bigCard.text

        override fun concreteClass() = bigCard::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_big_card
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.tv.text = listItem.bigCard.text
        }
    }
}