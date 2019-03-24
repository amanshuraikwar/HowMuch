package io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary

import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.item_expense_category_summary.view.*

class ExpenseCategorySummaryViewHolder(itemView: View) : ViewHolder<ExpenseCategorySummaryListItem>(itemView) {

    private val TAG = Util.getTag(this)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_expense_category_summary
    }

    override fun bind(listItem: ExpenseCategorySummaryListItem, host: androidx.fragment.app.FragmentActivity) {
        itemView.nameTv.text = listItem.expenseCategorySummary.name
        itemView.plannedTv.text = listItem.expenseCategorySummary.planned
        itemView.actualTv.text = listItem.expenseCategorySummary.actual

        val actual = listItem.expenseCategorySummary.actual.toFloat()
        val planned = listItem.expenseCategorySummary.planned.toFloat()

        var progress = actual * 100 / planned
        var secondaryProgress = 0f
        itemView.pbIconIv.visibility = INVISIBLE

        if (actual > planned) {
            progress = planned * 100 / (actual)
            secondaryProgress = 100f
            itemView.pbIconIv.visibility = VISIBLE
        }

        Log.wtf(TAG, progress.toString() + " " + secondaryProgress.toString())

        itemView.pb.progress = progress.toInt()
        itemView.pb.secondaryProgress = secondaryProgress.toInt()
    }
}