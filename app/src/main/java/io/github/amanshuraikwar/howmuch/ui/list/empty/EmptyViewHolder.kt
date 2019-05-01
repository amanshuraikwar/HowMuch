package io.github.amanshuraikwar.howmuch.ui.list.empty

import android.graphics.drawable.Drawable
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import android.view.View
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.howmuch.R
import kotlinx.android.synthetic.main.item_empty.view.*

class EmptyViewHolder(itemView: View) : ViewHolder<EmptyListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_empty
    }

    override fun bind(listItem: EmptyListItem, host: FragmentActivity) {

        val animated =
                AnimatedVectorDrawableCompat.create(host, R.drawable.avd_face_looking)

        animated?.registerAnimationCallback(
                object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        itemView.iv.post { animated.start() }
                    }
                }
        )

        itemView.iv.setImageDrawable(animated)
        animated?.start()

    }
}