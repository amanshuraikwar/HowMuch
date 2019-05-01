package io.github.amanshuraikwar.multiitemlistadapter;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * Base ViewHolder used throughout the app.
 * Complies with the RecyclerViewAdapter that uses visitable design pattern.
 *
 * @author Amanshu Raikwar
 * Created by sonu on 30/6/17.
 */

public abstract class ViewHolder<Item extends ListItem>
        extends RecyclerView.ViewHolder {

    private View itemView;

    public ViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    protected View getItemView() {
        return itemView;
    }

    public abstract void bind(Item listItem,
                              FragmentActivity host);
}
