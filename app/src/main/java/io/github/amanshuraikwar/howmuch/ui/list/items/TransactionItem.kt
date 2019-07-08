package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionItem(val transaction: Transaction,
                      val category: Category,
                      val last: Boolean = false) {

    class Item(val obj: TransactionItem)
        : ListItem<TransactionOnClickListener, ListItemTypeFactory>() {

        override fun id() = obj.transaction.id

        override fun concreteClass() = obj::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_transaction
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val transaction = listItem.obj.transaction

            itemView.titleTv.text = transaction.title
            itemView.amountTv.text = transaction.amount.toString()
            itemView.timeTv.text = Util.beautifyTime(transaction.time)

            itemView.iconIv.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor(listItem.obj.category.name)
                    )
            )

            ContextCompat.getColor(
                    host,
                    ViewUtil.getCategoryColor2(listItem.obj.category.name)
            ).let {
                itemView.amountCv.setCardBackgroundColor(it)
                itemView.topLine.setBackgroundColor(it)
                itemView.lineV.setBackgroundColor(it)
            }

            itemView.iconIv.setImageResource(
                    ViewUtil.getCategoryIcon(listItem.obj.category.name)
            )

            itemView.parentCl.setOnClickListener {
                listItem.onClickListener.onClick(transaction)
            }

            if (listItem.obj.last) {
                itemView.lineV.visibility = View.GONE
            } else {
                itemView.lineV.visibility = View.VISIBLE
            }
        }
    }
}