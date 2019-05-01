package io.github.amanshuraikwar.howmuch.ui.spinner

import android.content.Context
import io.github.amanshuraikwar.howmuch.protocol.Category

class CategoryAdapter
constructor(context: Context, resourceId: Int, objects: List<Category>)
    : SpinnerAdapter<Category>(context, resourceId, objects) {

    override fun Category.getTitle() = this.name
}