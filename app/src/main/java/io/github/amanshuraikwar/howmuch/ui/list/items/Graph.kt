package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.graph.spark.GraphAdapter
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_graph.view.*

class Graph(val title: String,
            val yData: List<Float>,
            val xAxisLabels: List<String>) {

    class Item(val graph: Graph)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = graph.title

        override fun concreteClass() = graph::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_graph
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.sv.adapter =
                    GraphAdapter(
                            listItem.graph.yData.toFloatArray(),
                            listItem.graph.xAxisLabels.toTypedArray()
                    )
        }
    }
}