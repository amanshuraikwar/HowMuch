package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
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

            if (listItem.statCategory.category.type == TransactionType.CREDIT) {
                itemView.amountTv.text = "+${listItem.statCategory.amount}"
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            } else {
                itemView.amountTv.text = "-${listItem.statCategory.amount}"
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.red))
            }

            itemView.parentCl.setOnClickListener {
                listItem.statCategory.onClick.invoke(listItem.statCategory.category)
            }
        }
    }
}