package io.github.amanshuraikwar.howmuch.ui.list.items

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_total.view.*

class StatTotal(val creditAmount: Double,
                val creditTrend: Int,
                val debitAmount: Double,
                val debitTrend: Int,
                val onCreditClicked: () -> Unit,
                val onDebitClicked: () -> Unit) {

    class Item(val statTotal: StatTotal)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = "stat-total"

        override fun concreteClass() = statTotal::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_stat_total
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.amountTv.text = "+${listItem.statTotal.creditAmount}"
            itemView.debitAmountTv.text = "-${listItem.statTotal.debitAmount}"

            var statsDrawableResId =
                    when {
                        listItem.statTotal.creditTrend > 0 -> R.drawable.avd_stats_up
                        listItem.statTotal.creditTrend == 0 -> R.drawable.avd_stats_flat
                        else -> R.drawable.avd_stats_down
                    }

            val animated1 =
                    AnimatedVectorDrawableCompat.create(host, statsDrawableResId)

            animated1?.registerAnimationCallback(
                    object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            itemView.creditTrendIv.post { animated1.start() }
                        }
                    }
            )

            itemView.creditTrendIv.setImageDrawable(animated1)
            animated1?.start()

            if (listItem.statTotal.creditTrend >= 0) {
                itemView.creditTrendTv.text = "+${listItem.statTotal.creditTrend}%"
            } else {
                itemView.creditTrendTv.text = "${listItem.statTotal.creditTrend}%"
            }

            statsDrawableResId =
                    when {
                        listItem.statTotal.debitTrend > 0 -> R.drawable.avd_stats_up
                        listItem.statTotal.debitTrend == 0 -> R.drawable.avd_stats_flat
                        else -> R.drawable.avd_stats_down
                    }

            val animated2 =
                    AnimatedVectorDrawableCompat.create(host, statsDrawableResId)

            animated2?.registerAnimationCallback(
                    object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            itemView.debitTrendIv.post { animated2.start() }
                        }
                    }
            )

            itemView.debitTrendIv.setImageDrawable(animated2)
            animated2?.start()

            if (listItem.statTotal.debitTrend > 0) {
                itemView.debitTrendTv.text = "+${listItem.statTotal.debitTrend}%"
            } else {
                itemView.debitTrendTv.text = "${listItem.statTotal.debitTrend}%"
            }

            itemView.creditCv.setOnClickListener {
                listItem.statTotal.onCreditClicked.invoke()
            }

            itemView.debitCv.setOnClickListener {
                listItem.statTotal.onDebitClicked.invoke()
            }
        }
    }
}