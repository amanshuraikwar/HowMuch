package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_icon_title.view.*

class IconTitle(val title: String,
                val iconResId: Int,
                val color1ResId: Int,
                val color2ResId: Int) {

    class Item(val iconTitle: IconTitle)
        : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

        override fun id() = iconTitle.title

        override fun concreteClass() = iconTitle::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_icon_title
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.titleTv.text = listItem.iconTitle.title

            itemView.iconIv.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                            host,
                            listItem.iconTitle.color1ResId
                    )
            )

            itemView.iconBgCv.setCardBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            listItem.iconTitle.color2ResId
                    )
            )

            itemView.iconIv.setImageResource(listItem.iconTitle.iconResId)
        }
    }
}