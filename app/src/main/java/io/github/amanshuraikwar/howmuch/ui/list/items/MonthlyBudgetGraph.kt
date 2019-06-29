package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_category.view.*

class MonthlyBudgetGraph {

    class Item(val obj: MonthlyBudgetGraph)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = "MonthlyBudgetGraph"

        override fun concreteClass() = obj::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_monthly_budget_graph
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

        }
    }
}