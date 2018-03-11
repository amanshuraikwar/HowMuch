package io.github.amanshuraikwar.howmuch.ui.expenseday

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ViewHolder
import io.github.amanshuraikwar.howmuch.util.NumberUtil

/**
 * Created by amanshuraikwar on 11/03/18.
 */
class ExpenseDayViewHolder(itemView: View) : ViewHolder<ExpenseDayListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_transaction_day
    }

    @BindView(R.id.dateTv)
    lateinit var dateTv: TextView

    @BindView(R.id.transactionsTv)
    lateinit var transactionsTv: TextView

    @BindView(R.id.progressBar)
    lateinit var progressBar: RoundCornerProgressBar

    @BindView(R.id.spentAmountTv)
    lateinit var spentAmountTv: TextView

    @BindView(R.id.otherAmountTv)
    lateinit var otherAmountTv: TextView

    @BindView(R.id.otherTextTv)
    lateinit var otherTextTv: TextView

    override fun bind(listItem: ExpenseDayListItem,
                      parentActivity: FragmentActivity) {

        dateTv.text = NumberUtil.formatDate(listItem.getDayExpense().date)
        transactionsTv.text = "${listItem.getDayExpense().transactionNum} transactions"

        val spent = listItem.getDayExpense().amount
        val diff = listItem.getDailyLimit() - listItem.getDayExpense().amount

        val greenColor = ContextCompat.getColor(parentActivity, R.color.lightGreen)
        val redColor = ContextCompat.getColor(parentActivity, R.color.red)
        val lightBlueColor = ContextCompat.getColor(parentActivity, R.color.lightBlue)
        val veryLightBlueColor = ContextCompat.getColor(parentActivity, R.color.veryLightBlue)
        val grey3Color = ContextCompat.getColor(parentActivity, R.color.grey3)

        if (diff < 0) {
            progressBar.max = (spent - diff).toFloat()
            progressBar.progress = spent.toFloat()
            progressBar.progressColor = greenColor
            progressBar.secondaryProgress = progressBar.max
            progressBar.secondaryProgressColor = redColor
            otherAmountTv.setTextColor(redColor)
            otherTextTv.setTextColor(redColor)
            otherAmountTv.text = "₹${(-diff)}"
            otherTextTv.text = parentActivity.getString(R.string.above_limit)
            progressBar.progressBackgroundColor = veryLightBlueColor
        } else {
            progressBar.max = (spent + diff).toFloat()
            progressBar.progress = spent.toFloat()
            progressBar.progressColor = lightBlueColor
            progressBar.secondaryProgress = progressBar.max
            progressBar.secondaryProgressColor = greenColor
            otherAmountTv.setTextColor(greenColor)
            otherTextTv.setTextColor(greenColor)
            otherAmountTv.text = "₹${(diff)}"
            otherTextTv.text = parentActivity.getString(R.string.left)
            progressBar.progressBackgroundColor = grey3Color
        }
        spentAmountTv.text = "₹$spent"
    }
}