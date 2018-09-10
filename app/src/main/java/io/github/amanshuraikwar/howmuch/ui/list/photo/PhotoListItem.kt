package io.github.amanshuraikwar.howmuch.ui.list.photo

import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.SimpleListItemOnClickListener
import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory

/**
 * ListItem representing a Photo in the RecyclerView.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 11/03/18.
 */
@Suppress("MemberVisibilityCanBePrivate")
class PhotoListItem(val photo: Photo)
    : ListItem<SimpleListItemOnClickListener, ListItemTypeFactory>() {

    override fun id() = photo.id

    override fun concreteClass() = PhotoListItem::class.toString()

    override fun type(typeFactory: ListItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}