package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.TransactionType
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_edit_category.view.*
import kotlinx.android.synthetic.main.item_edit_category.view.deleteIb
import kotlinx.android.synthetic.main.item_edit_category.view.editIb

class EditCategory(val category: Category,
                   val onEditSaveClicked: (Category) -> Unit,
                   val onDeleteClicked: (Category) -> Unit) {

    class Item(val editCategory: EditCategory)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = editCategory.category.id

        override fun concreteClass() = editCategory::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_edit_category
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            val category = listItem.editCategory.category

            itemView.categoryNameEt.setText(category.name)

            if (category.type == TransactionType.DEBIT) {
                itemView.categoryTypeRg.check(R.id.debitRb)
                itemView.categoryIconIv.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.red)
                )
            } else {
                itemView.categoryTypeRg.check(R.id.creditRb)
                itemView.categoryIconIv.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.green)
                )
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
                    itemView.parentFl.isRaised = true
                    itemView.parentFl.refreshDrawableState()
                } else {
                    itemView.deleteIb.visibility = GONE
                    itemView.categoryIconIv.visibility = VISIBLE
                    itemView.doneIb.visibility = GONE
                    itemView.editIb.visibility = VISIBLE
                    itemView.parentFl.isRaised = false
                    itemView.parentFl.refreshDrawableState()
                }
            }

            itemView.editIb.setOnClickListener {
                itemView.categoryNameEt.requestFocus()
            }

            itemView.deleteIb.setOnClickListener {
                listItem.editCategory.onDeleteClicked.invoke(category)
            }

            itemView.doneIb.setOnClickListener {
                listItem.editCategory.onEditSaveClicked.invoke(
                        category.copy(name = itemView.categoryNameEt.text.toString())
                )
            }
        }
    }
}