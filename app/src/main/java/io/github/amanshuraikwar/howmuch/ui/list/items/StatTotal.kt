package io.github.amanshuraikwar.howmuch.ui.list.items

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_stat_last_7_days.view.*

class StatTotal(val title: String,
                val amount: Double,
                val trend: Int) {

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
            val LAYOUT = R.layout.item_stat_last_7_days
        }

        override fun bind(listItem: Item, host: FragmentActivity) {
            itemView.titleTv.text = listItem.statTotal.title
            itemView.amountTv.text = "${listItem.statTotal.amount}"
            setTrendIcon(listItem.statTotal.trend, host)
        }

        private fun setTrendIcon(trend: Int, host: FragmentActivity) {

            val statsDrawableResId =
                    when {
                        trend > 0 -> R.drawable.avd_stats_up
                        trend == 0 -> R.drawable.avd_stats_flat
                        else -> R.drawable.avd_stats_down
                    }

            val animated =
                    AnimatedVectorDrawableCompat.create(host, statsDrawableResId)

            animated?.registerAnimationCallback(
                    object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            itemView.trendIv.post { animated.start() }
                        }
                    }
            )

            itemView.trendIv.setImageDrawable(animated)
            animated?.start()

            itemView.trendIv.setColorFilter(
                    when {
                        trend > 0 -> ContextCompat.getColor(host, R.color.green)
                        trend == 0 -> ContextCompat.getColor(host, R.color.activeIcon)
                        else -> ContextCompat.getColor(host, R.color.red)
                    }
            )
        }
    }
}