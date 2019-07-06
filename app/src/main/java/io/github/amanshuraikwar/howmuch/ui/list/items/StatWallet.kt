package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_wallet.view.*

class StatWallet(val wallet: Wallet,
                 val amount: Double,
                 val onClick: (Wallet) -> Unit) {

    class Item(val statWallet: StatWallet)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = statWallet.wallet.id

        override fun concreteClass() = statWallet::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_wallet
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.amountTv.text = listItem.statWallet.wallet.name

            if (listItem.statWallet.amount >= 0) {
                itemView.amountTv.text = "+${listItem.statWallet.amount}"
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            } else {
                itemView.amountTv.text = listItem.statWallet.amount.toString()
                itemView.amountTv.setTextColor(ContextCompat.getColor(host, R.color.red))
            }

            itemView.parentCl.setOnClickListener {
                listItem.statWallet.onClick.invoke(listItem.statWallet.wallet)
            }
        }
    }
}