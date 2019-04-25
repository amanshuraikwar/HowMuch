package io.github.amanshuraikwar.howmuch.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.amanshuraikwar.howmuch.R


class RaisableFrameLayout : FrameLayout {

    companion object {
        private val STATE_RAISED = intArrayOf(R.attr.state_raised)
    }

    var isRaised = false

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        var drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isRaised) {
            drawableState = mergeDrawableStates(drawableState, STATE_RAISED)
        }
        return drawableState
    }
}