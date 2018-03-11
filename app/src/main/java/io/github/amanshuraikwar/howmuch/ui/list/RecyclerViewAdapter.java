package io.github.amanshuraikwar.howmuch.ui.list;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 30/6/17.
 * recyclerview adapter which supports different type of viewholders efficiently
 * using visitable design pattern
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<ListItem> listItems;
    private ListItemTypeFactory typeFactory;
    private FragmentActivity parentActivity;

    public RecyclerViewAdapter(@NonNull FragmentActivity activity,
                               @NonNull ListItemTypeFactory typeFactory,
                               @NonNull List<ListItem> listItems) {
        this.typeFactory = typeFactory;
        this.parentActivity = activity;
        this.listItems = listItems;
    }

    public RecyclerViewAdapter(@NonNull FragmentActivity parentActivity,
                               @NonNull ListItemTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.parentActivity = parentActivity;
        listItems = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView =
                LayoutInflater
                        .from(parentActivity)
                        .inflate(typeFactory.getLayout(viewType), parent, false);

        return typeFactory.createViewHolder(contactView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(listItems.get(position), parentActivity);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public boolean isEmpty() {
        return listItems.size() == 0;
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).type(typeFactory);
    }

    public FragmentActivity getActivity() {
        return parentActivity;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public ListItem getListItem(int index) {
        return listItems.get(index);
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public void addListItems(List<ListItem> newListItems) {
        listItems.addAll(newListItems);
    }

    public void addListItem(ListItem newListItem) {
        listItems.add(newListItem);
    }

    public void removeListItem(int index) {
        listItems.remove(index);
    }
}
