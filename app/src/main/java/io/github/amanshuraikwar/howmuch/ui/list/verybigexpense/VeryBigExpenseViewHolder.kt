package io.github.amanshuraikwar.howmuch.ui.list.verybigexpense

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ViewHolder
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by amanshuraikwar on 11/03/18.
 */
class VeryBigExpenseViewHolder(itemView: View) : ViewHolder<VeryBigExpenseListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_expense_very_big
    }

    @BindView(R.id.parentLll)
    lateinit var parentLll: LinearLayout

    @BindView(R.id.expenseHeadingTvv)
    lateinit var expenseHeadingTvv: TextView

    @BindView(R.id.currencyTvv)
    lateinit var currencyTvv: TextView

    @BindView(R.id.amountTvv)
    lateinit var amountTvv: TextView

    override fun bind(listItem: VeryBigExpenseListItem,
                      parentActivity: FragmentActivity) {

        parentLll.setBackgroundColor(listItem.backgroundColor)
        expenseHeadingTvv.text = listItem.expenseHeading
        expenseHeadingTvv.setTextColor(listItem.primaryColor)
        currencyTvv.text = "₹"
        currencyTvv.setTextColor(listItem.secondaryColor)
        amountTvv.text = listItem.expenseAmount.toString()
        amountTvv.setTextColor(listItem.primaryColor)
    }
}