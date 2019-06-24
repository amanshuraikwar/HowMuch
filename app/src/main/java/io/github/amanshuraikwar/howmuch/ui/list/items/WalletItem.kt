package io.github.amanshuraikwar.howmuch.ui.list.items

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletItem(val wallet: Wallet) {

    interface WalletOnClickListener {
        fun onClick(wallet: Wallet)
    }

    class Item(val walletItem: WalletItem)
        : ListItem<WalletOnClickListener, ListItemTypeFactory>() {

        override fun id() = walletItem.wallet.id

        override fun concreteClass() = walletItem::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_wallet
        }

        private var statsDrawableResId: Int? = null
        private val lastAnimated: AnimatedVectorDrawableCompat? = null

        override fun bind(listItem: Item, host: FragmentActivity) {

            itemView.nameTv.text = listItem.walletItem.wallet.name
            itemView.balanceTv.text = listItem.walletItem.wallet.balance.toString()
            itemView.parentCl.setOnClickListener {
                listItem.onClickListener.onClick(listItem.walletItem.wallet)
            }

            var statsDrawableResId = R.drawable.avd_stats_up

            if (listItem.walletItem.wallet.balance < 0) {
                statsDrawableResId = R.drawable.avd_stats_down
                itemView.transactionFlowIb.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.red)
                )
            } else {
                itemView.transactionFlowIb.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(host, R.color.green)
                )
            }

            if (this.statsDrawableResId != statsDrawableResId) {

                val animated =
                        AnimatedVectorDrawableCompat.create(host, statsDrawableResId)

                lastAnimated?.registerAnimationCallback(
                        object : Animatable2Compat.AnimationCallback() {
                            override fun onAnimationEnd(drawable: Drawable?) {
                                // do nothing
                            }

                        }
                )

                animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        itemView.transactionFlowIb.post { animated.start() }
                    }

                })

                itemView.transactionFlowIb.setImageDrawable(animated)
                animated?.start()
            }

            val balanceStr = "${itemView.balanceTv.text}"
            if (listItem.walletItem.wallet.balance < 0) {
                itemView.balanceTv.text = balanceStr.subSequence(1, balanceStr.length)
                itemView.balanceTv.setTextColor(ContextCompat.getColor(host, R.color.red))
            } else {
                itemView.balanceTv.text = balanceStr
                itemView.balanceTv.setTextColor(ContextCompat.getColor(host, R.color.green))
            }
        }
    }
}