package io.github.amanshuraikwar.howmuch.ui.list.items

import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.deleteIb
import kotlinx.android.synthetic.main.item_category.view.editIb

class CategoryItem(val category: Category,
                   val onEditSaveClicked: (Category) -> Unit,
                   val onDeleteClicked: (Category) -> Unit) {

    class Item(val categoryItem: CategoryItem)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = categoryItem.category.id

        override fun concreteClass() = categoryItem::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_category
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val category = listItem.categoryItem.category

            itemView.categoryNameEt.setText(category.name)

            if (category.type == TransactionType.DEBIT) {
                itemView.categoryTypeRg.check(R.id.debitRb)
            } else {
                itemView.categoryTypeRg.check(R.id.creditRb)
            }

            itemView.deleteIb.visibility = GONE
            itemView.categoryIconIv.visibility = VISIBLE

            itemView.doneIb.visibility = GONE
            itemView.editIb.visibility = VISIBLE

            itemView.categoryNameEt.setOnFocusChangeListener {
                _, focus ->
                if (focus) {
                    itemView.deleteIb.visibility = VISIBLE
                    itemView.categoryIconIv.visibility = GONE
                    itemView.doneIb.visibility = VISIBLE
                    itemView.editIb.visibility = GONE
                    itemView.parentCl.elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, host.resources.displayMetrics)
                } else {
                    itemView.deleteIb.visibility = GONE
                    itemView.categoryIconIv.visibility = VISIBLE
                    itemView.doneIb.visibility = GONE
                    itemView.editIb.visibility = VISIBLE
                    itemView.parentCl.elevation = 0f
                }
            }

            itemView.editIb.setOnClickListener {
                itemView.categoryNameEt.requestFocus()
            }

            itemView.deleteIb.setOnClickListener {
                listItem.categoryItem.onDeleteClicked.invoke(category)
            }

            itemView.doneIb.setOnClickListener {
                listItem.categoryItem.onEditSaveClicked.invoke(
                        category.copy(
                                name = itemView.categoryNameEt.text.toString()
                                // todo
//                                type =
//                                if (itemView.categoryTypeRg.checkedRadioButtonId == R.id.debitRb)
//                                    TransactionType.DEBIT
//                                else
//                                    TransactionType.CREDIT
                        )
                )
            }
        }
    }
}