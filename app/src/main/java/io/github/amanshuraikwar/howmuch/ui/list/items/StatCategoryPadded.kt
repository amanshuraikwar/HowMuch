package io.github.amanshuraikwar.howmuch.ui.list.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_category_padded.view.*

class StatCategoryPadded(val category: Category,
                         val amount: Double,
                         val onClick: (Category) -> Unit) {

    class Item(val obj: StatCategoryPadded)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = obj.category.id

        override fun concreteClass() = obj::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_category_padded
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.titleTv.text = listItem.obj.category.name

            val percentage = ((listItem.obj.amount/listItem.obj.category.monthlyLimit) * 100).toInt()
            itemView.percentTv.text = " \u2022 $percentage%"

            itemView.parentCl.setOnClickListener {
                listItem.obj.onClick.invoke(listItem.obj.category)
            }

            itemView.iconIv.setColorFilter(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor(listItem.obj.category.name)
                    )
            )

            itemView.iconIv.setImageResource(
                    ViewUtil.getCategoryIcon(listItem.obj.category.name)
            )

            itemView.iconBgCv.setCardBackgroundColor(
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.obj.category.name)
                    )
            )

            itemView.pb.lineColor = ContextCompat.getColor(
                    host,
                    ViewUtil.getCategoryColor(listItem.obj.category.name)
            )

            itemView.pb.max = listItem.obj.category.monthlyLimit.toInt()
            itemView.pb.progress = listItem.obj.amount.toInt()

            itemView.pb.lineBackgroundColor =
                    ContextCompat.getColor(
                            host,
                            ViewUtil.getCategoryColor2(listItem.obj.category.name)
                    )

            itemView.pb.invalidate()
        }
    }
}