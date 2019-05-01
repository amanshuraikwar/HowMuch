package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_setting.view.*

class Setting(val title: String,
              val summary: String,
              val iconResId: Int) {

    class Item(val setting: Setting)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = setting.title

        override fun concreteClass() = setting::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_setting
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.iconIv.setImageResource(listItem.setting.iconResId)
            itemView.titleTv.text = listItem.setting.title
            itemView.summaryTv.text = listItem.setting.summary

            itemView.parentCl.setOnClickListener {
                listItem.onClickListener.onClick()
            }
        }
    }
}