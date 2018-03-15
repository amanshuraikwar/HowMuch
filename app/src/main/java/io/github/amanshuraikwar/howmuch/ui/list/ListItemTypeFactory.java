package io.github.amanshuraikwar.howmuch.ui.list;

import android.view.View;

import io.github.amanshuraikwar.howmuch.ui.list.bigexpense.BigExpenseListItem;
import io.github.amanshuraikwar.howmuch.ui.list.bigexpense.BigExpenseViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.expenseday.ExpenseDayListItem;
import io.github.amanshuraikwar.howmuch.ui.list.expenseday.ExpenseDayViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.header.HeaderListItem;
import io.github.amanshuraikwar.howmuch.ui.list.header.HeaderViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem;
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionViewHolder;

/**
 * Created by amanshuraikwar on 20/12/17.
 */

public class ListItemTypeFactory {

    public int type(ExpenseDayListItem item) {
        return 0;
    }

    public int type(TransactionListItem item) {
        return 1;
    }

    public int type(HeaderListItem item) {
        return 2;
    }

    public int type(BigExpenseListItem item) {
        return 3;
    }

    public int getLayout(int viewType) {
        switch (viewType) {
            case 0:
                return ExpenseDayViewHolder.Companion.getLAYOUT();
            case 1:
                return TransactionViewHolder.Companion.getLAYOUT();
            case 2:
                return HeaderViewHolder.Companion.getLAYOUT();
            case 3:
                return BigExpenseViewHolder.Companion.getLAYOUT();
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
            case 1:
                viewHolder = new TransactionViewHolder(parent);
                break;
            case 2:
                viewHolder = new HeaderViewHolder(parent);
                break;
            case 3:
                viewHolder = new BigExpenseViewHolder(parent);
                break;
        }

        return viewHolder;
    }
}
