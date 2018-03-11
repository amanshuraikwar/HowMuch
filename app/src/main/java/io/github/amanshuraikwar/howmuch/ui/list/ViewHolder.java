package io.github.amanshuraikwar.howmuch.ui.list;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by sonu on 30/6/17.
 */

public abstract class ViewHolder<Item extends ListItem>
        extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(Item listItem,
                              FragmentActivity parentActivity);
}
