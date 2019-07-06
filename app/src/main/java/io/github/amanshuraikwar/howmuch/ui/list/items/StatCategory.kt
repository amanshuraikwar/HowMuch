package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
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
                   val amount: Double,
                   val onClick: (Category) -> Unit) {

    class Item(val obj: StatCategory)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = obj.category.id

        override fun concreteClass() = obj::class.toString()

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

            itemView.titleTv.text = listItem.obj.category.name
            itemView.amountTv.text = listItem.obj.amount.toString()

            itemView.parentCl.setOnClickListener {
                listItem.obj.onClick.invoke(listItem.obj.category)
            }

            itemView.iconIv.setColorFilter(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor(listItem.obj.category.name)
                    )
            )

            itemView.iconIv.setImageResource(
                    ViewUtil.getCategoryIcon(listItem.obj.category.name)
            )

            itemView.iconBgCv.setCardBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.obj.category.name)
                    )
            )
        }
    }
}