package io.github.amanshuraikwar.howmuch.ui.list.header

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
class HeaderViewHolder(itemView: View) : ViewHolder<HeaderListItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_list_header
    }

    @BindView(R.id.headerTv)
    lateinit var headerTv: TextView

    override fun bind(listItem: HeaderListItem,
                      parentActivity: FragmentActivity) {

        headerTv.text = listItem.getHeading()
    }
}