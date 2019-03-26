package io.github.amanshuraikwar.howmuch.ui.list;

import androidx.annotation.NonNull;
import android.view.View;

import io.github.amanshuraikwar.howmuch.ui.list.date.DateListItem;
import io.github.amanshuraikwar.howmuch.ui.list.date.DateViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem;
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.expense.TransactionListItem;
import io.github.amanshuraikwar.howmuch.ui.list.expense.TransactionViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary.ExpenseCategorySummaryListItem;
import io.github.amanshuraikwar.howmuch.ui.list.expensecategorysummary.ExpenseCategorySummaryViewHolder;
import io.github.amanshuraikwar.multiitemlistadapter.BaseTypeFactory;
import io.github.amanshuraikwar.multiitemlistadapter.ViewHolder;

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
    public int type(TransactionListItem item) {
        return 1;
    }

    public int type(ExpenseCategorySummaryListItem item) {
        return 2;
    }

    public int type(DateListItem item) {
        return 3;
    }

    public int type(EmptyListItem item) {
        return 4;
    }

    public int type(UserInfo.Item item) {
        return 5;
    }

    public int type(Setting.Item item) {
        return 6;
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
                return TransactionViewHolder.Companion.getLAYOUT();
            case 2:
                return ExpenseCategorySummaryViewHolder.Companion.getLAYOUT();
            case 3:
                return DateViewHolder.Companion.getLAYOUT();
            case 4:
                return EmptyViewHolder.Companion.getLAYOUT();
            case 5:
                return UserInfo.Holder.Companion.getLAYOUT();
            case 6:
                return Setting.Holder.Companion.getLAYOUT();
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
                viewHolder = new TransactionViewHolder(parent);
                break;
            case 2:
                viewHolder = new ExpenseCategorySummaryViewHolder(parent);
                break;
            case 3:
                viewHolder = new DateViewHolder(parent);
                break;
            case 4:
                viewHolder = new EmptyViewHolder(parent);
                break;
            case 5:
                viewHolder = new UserInfo.Holder(parent);
                break;
            case 6:
                viewHolder = new Setting.Holder(parent);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
        }

        return viewHolder;
    }
}
