package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_monthly_budget.view.*
import kotlin.math.min

class MonthlyExpenseLimit(val actual: Double,
                          val limit: Double,
                          val onClick: () -> Unit) {

    class Item(val monthlyExpenseLimit: MonthlyExpenseLimit)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = "monthly_expense_limit"

        override fun concreteClass() = monthlyExpenseLimit::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_monthly_budget
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.actualTv.text = "${listItem.monthlyExpenseLimit.actual}"
            itemView.limitTv.text = " / ${listItem.monthlyExpenseLimit.limit.toInt()}"

            var progress = 1.0

            if (listItem.monthlyExpenseLimit.limit != 0.0) {
                progress = min(
                        1.0,
                        listItem.monthlyExpenseLimit.actual / listItem.monthlyExpenseLimit.limit
                )
            }

            itemView.pb.max = 100
            itemView.pb.progress = (progress * 100).toInt()
            itemView.pb.invalidate()

            itemView.seeMoreBtn.setOnClickListener { listItem.monthlyExpenseLimit.onClick.invoke() }
        }
    }
}