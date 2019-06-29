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

class StatCategory(val category: Category,
                   val color: Int,
                   val icon: Int,
                   val amount: Double,
                   val onClick: (Category) -> Unit) {

    class Item(val statCategory: StatCategory)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = statCategory.category.id

        override fun concreteClass() = statCategory::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_category
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.titleTv.text = listItem.statCategory.category.name
            itemView.amountTv.text = "${listItem.statCategory.amount}"

            itemView.parentCl.setOnClickListener {
                listItem.statCategory.onClick.invoke(listItem.statCategory.category)
            }

            itemView.iconIv.setColorFilter(
                    ContextCompat.getColor(host, listItem.statCategory.color)
            )

            itemView.parentCv.setCardBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.statCategory.category.name)
                    )
            )

            itemView.iconIv.setImageResource(listItem.statCategory.icon)

            itemView.limitTv.text = " / ${listItem.statCategory.category.monthlyLimit}"

            if (listItem.statCategory.amount > listItem.statCategory.category.monthlyLimit) {
                itemView.warningIv.visibility = VISIBLE
                itemView.warningTv.visibility = VISIBLE
                itemView.warningTv.text = "You have exceeded your monthly limit by ${listItem.statCategory.amount - listItem.statCategory.category.monthlyLimit}"
            } else {
                itemView.warningIv.visibility = GONE
                itemView.warningTv.visibility = GONE
            }


            itemView.pb.lineColor = ContextCompat.getColor(host, listItem.statCategory.color)
            itemView.pb.max = listItem.statCategory.category.monthlyLimit.toInt()
            itemView.pb.progress = listItem.statCategory.amount.toInt()
            itemView.pb.lineBackgroundColor =
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor3(listItem.statCategory.category.name)
                    )
            itemView.pb.setBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.statCategory.category.name)
                    )
            )
            itemView.pb.invalidate()
        }
    }
}