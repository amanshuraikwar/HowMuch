package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.graph.BudgetLineView
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_monthly_budget_graph.view.*

class MonthlyBudgetGraph(val data: List<BudgetLineView.Item>,
                         val budgetLimit: Float,
                         val today: Float,
                         val isCurMonth: Boolean = true) {

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

            itemView.budgetLineView.budgetLimit = listItem.obj.budgetLimit
            itemView.budgetLineView.today = listItem.obj.today.toInt()
            itemView.budgetLineView.maxDate = 31
            itemView.budgetLineView.isCurMonth = listItem.obj.isCurMonth
            itemView.budgetLineView.data = listItem.obj.data
            itemView.budgetLineView.isChecked = false
            itemView.budgetLineView.invalidate()
        }
    }
}