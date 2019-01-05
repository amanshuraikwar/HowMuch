package io.github.amanshuraikwar.howmuch.ui.onboarding

import android.content.Context
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet


class CustomViewPager(context: Context, attrs: AttributeSet) : androidx.viewpager.widget.ViewPager(context, attrs) {

    private var pagingEnabled: Boolean = false

    init {
        this.pagingEnabled = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.pagingEnabled = enabled
    }
}