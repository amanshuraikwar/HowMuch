package io.github.amanshuraikwar.howmuch.ui.list;

import android.view.View;
import io.github.amanshuraikwar.howmuch.ui.expenseday.ExpenseDayListItem;
import io.github.amanshuraikwar.howmuch.ui.expenseday.ExpenseDayViewHolder;

/**
 * Created by amanshuraikwar on 20/12/17.
 */

public class ListItemTypeFactory {

    public int type(ExpenseDayListItem item) {
        return 0;
    }

    public int getLayout(int viewType) {
        switch (viewType) {
            case 0:
                return ExpenseDayViewHolder.Companion.getLAYOUT();
            default:
                return 0;
        }
    }

    public ViewHolder createViewHolder(View parent, int type) {

        ViewHolder viewHolder = null;

        switch (type) {
            case 0:
                viewHolder = new ExpenseDayViewHolder(parent);
                break;
        }

        return viewHolder;
    }
}
