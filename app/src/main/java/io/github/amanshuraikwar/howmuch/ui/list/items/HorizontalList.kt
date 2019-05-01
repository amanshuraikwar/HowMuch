package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_horizontal_list.view.*

interface HorizontalList {

    class Eager(val id: String, val items: List<ListItem<*, *>>) {

        class Item(val eager: HorizontalList.Eager)
            : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

            override fun id() = eager.id

            override fun concreteClass() = eager::class.toString()

            override fun type(typeFactory: ListItemTypeFactory): Int {
                return typeFactory.type(this)
            }
        }

        class Holder(itemView: View) : ViewHolder<Item>(itemView) {

            companion object {
                @LayoutRes
                val LAYOUT = R.layout.item_horizontal_list
            }

            override fun bind(listItem: Item, host: FragmentActivity) {

                itemView.itemsRv.layoutManager =
                        LinearLayoutManager(host, LinearLayoutManager.HORIZONTAL, false)

                itemView.itemsRv.adapter = MultiItemListAdapter(host, ListItemTypeFactory())

                (itemView.itemsRv.adapter as MultiItemListAdapter<*>)
                        .submitList(listItem.eager.items)
            }
        }
    }
}