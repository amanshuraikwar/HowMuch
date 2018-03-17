package io.github.amanshuraikwar.howmuch.ui.list.transaction

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
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
class TransactionViewHolder(itemView: View) : ViewHolder<TransactionListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_transaction
    }

    @BindView(R.id.spentAmountTvv)
    lateinit var spentAmountTv: TextView

    @BindView(R.id.spentCurrencyTvv)
    lateinit var spentCurrencyTvv: TextView

    @BindView(R.id.timeTv)
    lateinit var timeTv: TextView

    @BindView(R.id.progressBar)
    lateinit var progressBar: RoundCornerProgressBar

    override fun bind(listItem: TransactionListItem,
                      parentActivity: FragmentActivity) {

        progressBar.max = listItem.getMaxTransactionAmount().toFloat()
        progressBar.progress = listItem.getTransaction().getAmount().toFloat()
        spentAmountTv.text = Util.formatAmount(listItem.getTransaction().getAmount())

        spentCurrencyTvv.text = "₹"

        timeTv.text = Util.formatTime(listItem.getTransaction().getDateAdded())
    }
}