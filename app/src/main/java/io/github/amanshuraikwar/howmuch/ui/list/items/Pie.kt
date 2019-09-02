package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.graph.PieView
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_pie.view.*

class Pie(val items: List<PieView.PieItem>,
          val total: Double) {

    class Item(val pie: Pie)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = pie::class.toString()

        override fun concreteClass() = pie::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_pie
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.pieView.data = listItem.pie.items
            itemView.pieView.invalidate()
            itemView.totalTv.text = listItem.pie.total.toString()
        }
    }
}