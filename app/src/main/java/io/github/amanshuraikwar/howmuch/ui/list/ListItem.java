package io.github.amanshuraikwar.howmuch.ui.list;

/**
 * Created by amanshuraikwar on 20/12/17.
 */

public abstract class ListItem<OnClickListener> {

    private OnClickListener onClickListener;

    public abstract int type(ListItemTypeFactory typeFactory);

    public OnClickListener getOnClickListener(){
        return this.onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
}
