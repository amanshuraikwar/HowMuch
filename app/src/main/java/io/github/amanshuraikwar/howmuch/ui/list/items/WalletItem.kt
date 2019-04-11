package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletItem(val wallet: Wallet) {

    interface WalletOnClickListener {
        fun onClick(wallet: Wallet)
    }

    class Item(val walletItem: WalletItem)
        : ListItem<WalletOnClickListener, ListItemTypeFactory>() {

        override fun id() = walletItem.wallet.id

        override fun concreteClass() = walletItem::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_wallet
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.nameTv.text = listItem.walletItem.wallet.name
            itemView.balanceTv.text = listItem.walletItem.wallet.balance.toString()
            itemView.parentLl.setOnClickListener {
                listItem.onClickListener.onClick(listItem.walletItem.wallet)
            }

            if (listItem.walletItem.wallet.balance < 0) {
                itemView.balanceTv.setTextColor(ContextCompat.getColor(host, R.color.red))
            } else {
                itemView.balanceTv.text = "+${itemView.balanceTv.text}"
                itemView.balanceTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            }
        }
    }
}