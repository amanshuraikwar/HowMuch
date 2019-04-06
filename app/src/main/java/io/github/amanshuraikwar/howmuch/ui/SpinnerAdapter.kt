package io.github.amanshuraikwar.howmuch.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

abstract class SpinnerAdapter<T>
constructor(context: Context, var resourceId: Int, objects: List<T>)
    : ArrayAdapter<T>(context, resourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItem = convertView

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(resourceId, parent, false)

        (listItem as TextView).text = getItem(position)!!.getTitle()

        return listItem
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItem = convertView

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(resourceId, parent, false)

        (listItem as TextView).text = getItem(position)!!.getTitle()

        return listItem
    }

    abstract fun T.getTitle(): String
}