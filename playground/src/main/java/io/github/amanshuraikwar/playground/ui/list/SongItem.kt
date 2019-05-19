package io.github.amanshuraikwar.playground.ui.list

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder
import io.github.amanshuraikwar.playground.R
import io.github.amanshuraikwar.playground.data.Song
import io.github.amanshuraikwar.playground.ui.ListItemTypeFactory
import kotlinx.android.synthetic.main.item_song.view.*

class SongItem(val song: Song) {

    class Item(val songItem: SongItem)
        : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

        override fun id() = songItem.song.timeAdded.time.toString()

        override fun concreteClass() = songItem::class.toString()

        override fun type(typeFactory: ListItemTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    class Holder(itemView: View) : ViewHolder<Item>(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_song
        }

        override fun bind(listItem: Item, host: FragmentActivity) {

            if (listItem.songItem.song.artUrl == "") {
                itemView.errorIconIv.visibility = VISIBLE
            } else {
                itemView.errorIconIv.visibility = GONE
            }

            Glide.with(host).load(listItem.songItem.song.artUrl).into(itemView.artIv)

            itemView.nameTv.text = listItem.songItem.song.name
            itemView.artistTv.text = listItem.songItem.song.artist
            itemView.albumTv.text = listItem.songItem.song.album

            itemView.signOutBtn.setOnClickListener {
                host.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(listItem.songItem.song.spotifyUrl))
                )
            }

            itemView.parentFl.isRaised = false
            itemView.parentFl.refreshDrawableState()
            itemView.artistIv.visibility = GONE
            itemView.albumTv.visibility = GONE
            itemView.albumIv.visibility = GONE
            itemView.signOutBtn.visibility = GONE

            itemView.parentFl.setOnClickListener {
                if (itemView.parentFl.isRaised) {
                    itemView.parentFl.isRaised = false
                    itemView.parentFl.refreshDrawableState()
                    itemView.artistIv.visibility = GONE
                    itemView.albumTv.visibility = GONE
                    itemView.albumIv.visibility = GONE
                    itemView.signOutBtn.visibility = GONE
                } else {
                    itemView.parentFl.isRaised = true
                    itemView.parentFl.refreshDrawableState()
                    itemView.albumTv.visibility = VISIBLE
                    itemView.artistIv.visibility = VISIBLE
                    itemView.albumTv.visibility = VISIBLE
                    itemView.albumIv.visibility = VISIBLE
                    itemView.signOutBtn.visibility = VISIBLE
                }
            }
        }
    }
}