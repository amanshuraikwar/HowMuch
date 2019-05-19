package io.github.amanshuraikwar.playground.ui;

import android.view.View;

import androidx.annotation.NonNull;

import io.github.amanshuraikwar.multiitemlistadapter.BaseTypeFactory;
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder;
import io.github.amanshuraikwar.playground.ui.list.SongItem;
import io.github.amanshuraikwar.playground.ui.list.date.HeaderListItem;
import io.github.amanshuraikwar.playground.ui.list.date.HeaderViewHolder;

/**
 * List item type factory responsible for getting layout id and ViewHolder instance.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 20/12/17.
 */
@SuppressWarnings("unused")
public class ListItemTypeFactory extends BaseTypeFactory {

    /**
     * To get type for a list item
     * @param item list item
     * @return type of the provided list item
     */
    public int type(SongItem.Item item) {
        return 1;
    }

    public int type(HeaderListItem item) {
        return 2;
    }

    /**
     * To get layout file id (R.layout.*) for a corresponding list item/view type.
     *
     * @param viewType list item/view type.
     * @return layout file id corresponding to list item/view type.
     */
    public int getLayout(int viewType) {
        switch (viewType) {
            case 1:
                return SongItem.Holder.Companion.getLAYOUT();
            case 2:
                return HeaderViewHolder.Companion.getLAYOUT();
            default:
                return super.getLayout(viewType);
        }
    }

    /**
     * To get ViewHolder instance for corresponding list item/view type.
     *
     * @param parent parent view instance to instantiate corresponding ViewHolder instance.
     * @param type list item/view type of which the ViewHolder instance is needed.
     * @return ViewHolder instance for the given list item/view type.
     */
    public ViewHolder createViewHolder(@NonNull View parent, int type) {

        ViewHolder viewHolder;

        switch (type) {
            case 1:
                viewHolder = new SongItem.Holder(parent);
                break;
            case 2:
                viewHolder = new HeaderViewHolder(parent);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
        }

        return viewHolder;
    }
}
