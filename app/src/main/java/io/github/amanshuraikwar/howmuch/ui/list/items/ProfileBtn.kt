package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_profile_btn.view.*

class ProfileBtn(val title: String,
                 val iconResId: Int) {

    class Item(val profileBtn: ProfileBtn)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = profileBtn.title

        override fun concreteClass() = profileBtn::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_profile_btn
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.iconIv.setImageResource(listItem.profileBtn.iconResId)
            itemView.titleTv.text = listItem.profileBtn.title
            itemView.parentFl.setOnClickListener {
                listItem.onClickListener.onClick()
            }
        }
    }
}