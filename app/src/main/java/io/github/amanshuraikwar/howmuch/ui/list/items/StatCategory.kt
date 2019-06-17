package io.github.amanshuraikwar.howmuch.ui.list.items

import android.graphics.ColorFilter
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
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

            itemView.iconIv.setImageResource(listItem.statCategory.icon)
        }
    }
}