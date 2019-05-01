package io.github.amanshuraikwar.multiitemlistadapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class MultiItemListAdapter<TypeFactory extends BaseTypeFactory> extends ListAdapter<ListItem, ViewHolder> {


    private static DiffUtil.ItemCallback<ListItem> LIST_ITEM_COMPARATOR = new DiffUtil.ItemCallback<ListItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull ListItem listItem1,
                                       @NonNull ListItem listItem2) {
            return (listItem1.concreteClass() + listItem1.id())
                    .equals(listItem2.concreteClass() + listItem2.id());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ListItem listItem1,
                                          @NonNull ListItem listItem2) {
            return listItem1 == listItem2;
        }
    };

    protected FragmentActivity host;
    private TypeFactory typeFactory;

    public MultiItemListAdapter(@NonNull FragmentActivity host,
                                   @NonNull TypeFactory typeFactory) {
        super(LIST_ITEM_COMPARATOR);
        this.host = host;
        this.typeFactory = typeFactory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contactView =
                LayoutInflater
                        .from(host)
                        .inflate(typeFactory.getLayout(viewType), parent, false);

        return typeFactory.createViewHolder(contactView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), host);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type(typeFactory);
    }
}
