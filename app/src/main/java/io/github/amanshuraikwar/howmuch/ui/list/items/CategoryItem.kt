package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryItem(val icon: Int,
                   val color: Int,
                   val bgColor: Int) {

    class Item(val category: CategoryItem)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = "x"

        override fun concreteClass() = category::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_category
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.iconIv.setImageResource(listItem.category.icon)

            itemView.iconIv.setColorFilter(
                    ContextCompat.getColor(host, listItem.category.color)
            )

            itemView.cv.setCardBackgroundColor(
                    ContextCompat.getColor(host, listItem.category.bgColor)
            )
        }
    }
}