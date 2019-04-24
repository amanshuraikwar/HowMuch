package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_edit_wallet.view.*

class EditWallet(val wallet: Wallet,
                 val onEditSaveClicked: (Wallet) -> Unit,
                 val onDeleteClicked: (Wallet) -> Unit) {

    class Item(val editWallet: EditWallet)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = editWallet.wallet.id

        override fun concreteClass() = editWallet::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_edit_wallet
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val wallet = listItem.editWallet.wallet

            itemView.walletNameEt.setText(wallet.name)
            itemView.walletBalanceEt.setText(wallet.balance.toString())

            itemView.deleteIb.visibility = GONE
            itemView.walletIconIv.visibility = VISIBLE

            itemView.doneIb.visibility = GONE
            itemView.editIb.visibility = VISIBLE

            val focusChangeListener: (View, Boolean) -> Unit = {
                _, focus ->
                if (focus) {
                    itemView.deleteIb.visibility = VISIBLE
                    itemView.walletIconIv.visibility = GONE
                    itemView.doneIb.visibility = VISIBLE
                    itemView.editIb.visibility = GONE
                    itemView.parentFl.isRaised = true
                    itemView.parentFl.refreshDrawableState()
                } else {
                    itemView.deleteIb.visibility = GONE
                    itemView.walletIconIv.visibility = VISIBLE
                    itemView.doneIb.visibility = GONE
                    itemView.editIb.visibility = VISIBLE
                    itemView.parentFl.isRaised = false
                    itemView.parentFl.refreshDrawableState()
                }
            }

            itemView.walletNameEt.setOnFocusChangeListener(focusChangeListener)
            itemView.walletBalanceEt.setOnFocusChangeListener(focusChangeListener)

            itemView.editIb.setOnClickListener {
                itemView.walletNameEt.requestFocus()
            }

            itemView.deleteIb.setOnClickListener {
                listItem.editWallet.onDeleteClicked.invoke(wallet)
            }

            itemView.doneIb.setOnClickListener {
                listItem.editWallet.onEditSaveClicked.invoke(
                        wallet.copy(name = itemView.walletNameEt.text.toString())
                )
            }
        }
    }
}