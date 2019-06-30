package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_limit.view.*
import kotlin.math.min

class Limit(val actual: Double,
            val limit: Double,
            val color1: Int,
            val color2: Int) {

    class Item(val limit: Limit)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = "monthly_expense_limit"

        override fun concreteClass() = limit::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_limit
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.actualTv.text = "${listItem.limit.actual}"
            itemView.limitTv.text = " / ${listItem.limit.limit.toInt()}"

            var progress = 1.0

            if (listItem.limit.limit != 0.0) {
                progress = min(
                        1.0,
                        listItem.limit.actual / listItem.limit.limit
                )
            }

            itemView.pb.max = 100
            itemView.pb.progress = (progress * 100).toInt()

            itemView.pb.lineColor = ContextCompat.getColor(
                    host,
                    listItem.limit.color1
            )

            itemView.pb.lineBackgroundColor = ContextCompat.getColor(
                    host,
                    listItem.limit.color2
            )

            itemView.pb.invalidate()
        }
    }
}