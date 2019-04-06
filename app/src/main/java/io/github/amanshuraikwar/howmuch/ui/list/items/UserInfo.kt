package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_user_info.view.*

class UserInfo(val name: String,
               val email: String,
               val photoUrl: String) {

    class Item(val userInfo: UserInfo)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = userInfo.name

        override fun concreteClass() = userInfo::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_user_info
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.nameTv.text = listItem.userInfo.name
            itemView.emailTv.text = listItem.userInfo.email
            Glide.with(host).load(listItem.userInfo.photoUrl).into(itemView.profilePicIv)
        }
    }
}