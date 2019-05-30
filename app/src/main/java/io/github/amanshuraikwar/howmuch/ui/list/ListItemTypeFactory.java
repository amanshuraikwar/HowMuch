package io.github.amanshuraikwar.howmuch.ui.list;

import androidx.annotation.NonNull;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderListItem;
import io.github.amanshuraikwar.howmuch.ui.list.date.HeaderViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyListItem;
import io.github.amanshuraikwar.howmuch.ui.list.empty.EmptyViewHolder;
import io.github.amanshuraikwar.howmuch.ui.list.items.BigCard;
import io.github.amanshuraikwar.howmuch.ui.list.items.Button;
import io.github.amanshuraikwar.howmuch.ui.list.items.EditCategory;
import io.github.amanshuraikwar.howmuch.ui.list.items.EditWallet;
import io.github.amanshuraikwar.howmuch.ui.list.items.Graph;
import io.github.amanshuraikwar.howmuch.ui.list.items.HorizontalList;
import io.github.amanshuraikwar.howmuch.ui.list.items.MonthlyExpenseLimit;
import io.github.amanshuraikwar.howmuch.ui.list.items.ProfileBtn;
import io.github.amanshuraikwar.howmuch.ui.list.items.Setting;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatButton;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatCategory;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatHeader;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatTotal;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatTransaction;
import io.github.amanshuraikwar.howmuch.ui.list.items.StatWallet;
import io.github.amanshuraikwar.howmuch.ui.list.items.Total;
import io.github.amanshuraikwar.howmuch.ui.list.items.UserInfo;
import io.github.amanshuraikwar.howmuch.ui.list.items.WalletItem;
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionListItem;
import io.github.amanshuraikwar.howmuch.ui.list.transaction.TransactionViewHolder;
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

    public int type(StatCategory.Item item) {
        return 2;
    }

    public int type(HeaderListItem item) {
        return 3;
    }

    public int type(EmptyListItem item) {
        return 4;
    }

    public int type(UserInfo.Item item) {
        return 5;
    }

    public int type(ProfileBtn.Item item) {
        return 6;
    }

    public int type(HorizontalList.Eager.Item item) {
        return 7;
    }

    public int type(WalletItem.Item item) {
        return 8;
    }

    public int type(StatTotal.Item item) {
        return 9;
    }

    public int type(Button.Item item) {
        return 10;
    }

    public int type(StatWallet.Item item) {
        return 11;
    }

    public int type(EditCategory.Item item) {
        return 12;
    }

    public int type(EditWallet.Item item) {
        return 13;
    }

    public int type(Setting.Item item) {
        return 14;
    }

    public int type(MonthlyExpenseLimit.Item item) {
        return 15;
    }

    public int type(BigCard.Item item) {
        return 16;
    }

    public int type(StatButton.Item item) {
        return 17;
    }

    public int type(StatHeader.Item item) {
        return 18;
    }

    public int type(StatTransaction.Item item) {
        return 19;
    }

    public int type(Graph.Item item) {
        return 20;
    }

    public int type(Total.Item item) {
        return 21;
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
                return StatCategory.Holder.Companion.getLAYOUT();
            case 3:
                return HeaderViewHolder.Companion.getLAYOUT();
            case 4:
                return EmptyViewHolder.Companion.getLAYOUT();
            case 5:
                return UserInfo.Holder.Companion.getLAYOUT();
            case 6:
                return ProfileBtn.Holder.Companion.getLAYOUT();
            case 7:
                return HorizontalList.Eager.Holder.Companion.getLAYOUT();
            case 8:
                return WalletItem.Holder.Companion.getLAYOUT();
            case 9:
                return StatTotal.Holder.Companion.getLAYOUT();
            case 10:
                return Button.Holder.Companion.getLAYOUT();
            case 11:
                return StatWallet.Holder.Companion.getLAYOUT();
            case 12:
                return EditCategory.Holder.Companion.getLAYOUT();
            case 13:
                return EditWallet.Holder.Companion.getLAYOUT();
            case 14:
                return Setting.Holder.Companion.getLAYOUT();
            case 15:
                return MonthlyExpenseLimit.Holder.Companion.getLAYOUT();
            case 16:
                return BigCard.Holder.Companion.getLAYOUT();
            case 17:
                return StatButton.Holder.Companion.getLAYOUT();
            case 18:
                return StatHeader.Holder.Companion.getLAYOUT();
            case 19:
                return StatTransaction.Holder.Companion.getLAYOUT();
            case 20:
                return Graph.Holder.Companion.getLAYOUT();
            case 21:
                return Total.Holder.Companion.getLAYOUT();
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
                viewHolder = new StatCategory.Holder(parent);
                break;
            case 3:
                viewHolder = new HeaderViewHolder(parent);
                break;
            case 4:
                viewHolder = new EmptyViewHolder(parent);
                break;
            case 5:
                viewHolder = new UserInfo.Holder(parent);
                break;
            case 6:
                viewHolder = new ProfileBtn.Holder(parent);
                break;
            case 7:
                viewHolder = new HorizontalList.Eager.Holder(parent);
                break;
            case 8:
                viewHolder = new WalletItem.Holder(parent);
                break;
            case 9:
                viewHolder = new StatTotal.Holder(parent);
                break;
            case 10:
                viewHolder = new Button.Holder(parent);
                break;
            case 11:
                viewHolder = new StatWallet.Holder(parent);
                break;
            case 12:
                viewHolder = new EditCategory.Holder(parent);
                break;
            case 13:
                viewHolder = new EditWallet.Holder(parent);
                break;
            case 14:
                viewHolder = new Setting.Holder(parent);
                break;
            case 15:
                viewHolder = new MonthlyExpenseLimit.Holder(parent);
                break;
            case 16:
                viewHolder = new BigCard.Holder(parent);
                break;
            case 17:
                viewHolder = new StatButton.Holder(parent);
                break;
            case 18:
                viewHolder = new StatHeader.Holder(parent);
                break;
            case 19:
                viewHolder = new StatTransaction.Holder(parent);
                break;
            case 20:
                viewHolder = new Graph.Holder(parent);
                break;
            case 21:
                viewHolder = new Total.Holder(parent);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
        }

        return viewHolder;
    }
}
