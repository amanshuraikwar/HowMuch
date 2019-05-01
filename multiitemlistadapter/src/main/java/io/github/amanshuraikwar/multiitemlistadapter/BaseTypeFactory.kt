package io.github.amanshuraikwar.multiitemlistadapter

import android.view.View

open class BaseTypeFactory {

    /**
     * To get layout file id (R.layout.*) for a corresponding list item/view type.
     *
     * @param viewType list item/view type.
     * @return layout file id corresponding to list item/view type.
     */
    @Suppress("WhenWithOnlyElse", "UNUSED_EXPRESSION")
    open fun getLayout(viewType: Int): Int {
        when (viewType) {
            else -> throw IllegalArgumentException("Invalid view type.")
        }
    }

    /**
     * To get ViewHolder instance for corresponding list item/view type.
     *
     * @param parent parent view instance to instantiate corresponding ViewHolder instance.
     * @param type list item/view type of which the ViewHolder instance is needed.
     * @return ViewHolder instance for the given list item/view type.
     */
    @Suppress("WhenWithOnlyElse", "UNREACHABLE_CODE", "UNUSED_EXPRESSION", "CanBeVal")
    open fun createViewHolder(parent: View, type: Int): ViewHolder<*>? {

        var viewHolder: ViewHolder<*>? = null

        when (type) {
            else -> throw IllegalArgumentException("Invalid view type.")
        }

        return viewHolder
    }
}