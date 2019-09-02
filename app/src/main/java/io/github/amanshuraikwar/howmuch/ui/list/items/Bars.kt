package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.graph.BarView2
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_last_7_days_bar_graph.view.*

class Bars(val items: List<BarView2.BarItem>) {

    class Item(val bars: Bars)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = bars::class.toString()

        override fun concreteClass() = bars::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_last_7_days_bar_graph
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.barView.data = listItem.bars.items
            itemView.barView.setChecked(false)
            itemView.barView.invalidate()
        }
    }
}