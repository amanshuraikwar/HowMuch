package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.graph.BarView
import io.github.amanshuraikwar.howmuch.graph.BarView2
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_month_bar_graph.view.*

class MonthBarGraph(val items: List<BarView.BarItem>,
                    val color1: Int,
                    val color2: Int,
                    val markerX: String = "TODAY") {

    class Item(val bars: MonthBarGraph)
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
            val LAYOUT = R.layout.item_month_bar_graph
        }

        override fun bind(listItem: Item,
                          host: FragmentActivity) {
            itemView.barView.data = listItem.bars.items
            itemView.barView.lineColor = ContextCompat.getColor(host, listItem.bars.color1)
            itemView.barView.lineBackgroundColor = ContextCompat.getColor(host, listItem.bars.color2)
            itemView.barView.labelTextColor = ContextCompat.getColor(host, R.color.white)
            itemView.barView.labelMarker = true
            itemView.barView.markerX = listItem.bars.markerX
            itemView.barView.labelMarkerColor = ContextCompat.getColor(host, listItem.bars.color1)
            itemView.barView.invalidate()
        }
    }
}