package io.github.amanshuraikwar.howmuch.ui.list.expenseday

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ViewHolder
import io.github.amanshuraikwar.howmuch.util.Util

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

    @BindView(R.id.spentAmountTvv)
    lateinit var spentAmountTv: TextView

    @BindView(R.id.otherAmountCurrencyTv)
    lateinit var otherAmountCurrencyTv: TextView

    @BindView(R.id.otherAmountTv)
    lateinit var otherAmountTv: TextView

    @BindView(R.id.otherTextTv)
    lateinit var otherTextTv: TextView

    @BindView(R.id.parentLl)
    lateinit var parentLl: View

    override fun bind(listItem: ExpenseDayListItem,
                      parentActivity: FragmentActivity) {

        dateTv.text = Util.formatDate(listItem.getDayExpense().date)
        transactionsTv.text = "${listItem.getDayExpense().transactionNum}"

        val spent = listItem.getDayExpense().amount
        val diff = listItem.getDailyLimit() - listItem.getDayExpense().amount

        val lightGreenColor = ContextCompat.getColor(parentActivity, R.color.lightGreen)
        val greenColor = ContextCompat.getColor(parentActivity, R.color.green)
        val lightRedColor = ContextCompat.getColor(parentActivity, R.color.lightRed)
        val redColor = ContextCompat.getColor(parentActivity, R.color.red)
        val blueColor = ContextCompat.getColor(parentActivity, R.color.blue)

        if (diff < 0) {
            progressBar.isReverse = true
            progressBar.max = (spent - diff).toFloat()
            progressBar.progress = -diff.toFloat()
            progressBar.progressColor = redColor
            otherAmountCurrencyTv.setTextColor(lightRedColor)
            otherAmountTv.setTextColor(redColor)
            otherTextTv.setTextColor(redColor)
            otherAmountTv.text = Util.formatAmount(-diff)
            otherTextTv.text = parentActivity.getString(R.string.above_limit)
        } else {
            progressBar.isReverse = false
            progressBar.max = (spent + diff).toFloat()
            progressBar.progress = spent.toFloat()
            progressBar.progressColor = blueColor
            otherAmountCurrencyTv.setTextColor(lightGreenColor)
            otherAmountTv.setTextColor(greenColor)
            otherTextTv.setTextColor(greenColor)
            otherAmountTv.text = Util.formatAmount(diff)
            otherTextTv.text = parentActivity.getString(R.string.left)
        }
        spentAmountTv.text = Util.formatAmount(spent)

        parentLl.setOnClickListener { listItem.onClickListener.onClick(listItem.getDayExpense()) }
    }
}