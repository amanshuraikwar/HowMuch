package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_button.view.*

class StatButton(val text: String) {

    class Item(val button: StatButton)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = button.text

        override fun concreteClass() = button::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_button
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.btn.text = listItem.button.text
            itemView.btn.setOnClickListener {
                listItem.onClickListener.onClick()
            }
        }
    }
}