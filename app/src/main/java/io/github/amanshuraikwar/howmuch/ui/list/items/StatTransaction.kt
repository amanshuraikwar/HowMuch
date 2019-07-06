package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_transaction.view.*

class StatTransaction(val transaction: Transaction,
                      val category: Category) {

    class Item(val statTransaction: StatTransaction)
        : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

        override fun id() = statTransaction.transaction.id

        override fun concreteClass() = statTransaction::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_transaction
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val transaction = listItem.statTransaction.transaction

            itemView.titleTv.text = transaction.title
            itemView.amountTv.text = transaction.amount.toString()

            itemView.iconIv.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor(listItem.statTransaction.category.name)
                    )
            )

            itemView.iconBgCv.setCardBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.statTransaction.category.name)
                    )
            )

            itemView.iconIv.setImageResource(
                    ViewUtil.getCategoryIcon(listItem.statTransaction.category.name)
            )

            itemView.parentCl.setOnClickListener {
                listItem.onClickListener.onClick(transaction)
            }
        }
    }
}