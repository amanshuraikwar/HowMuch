package io.github.amanshuraikwar.howmuch.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.amanshuraikwar.howmuch.R


class AspectRatioFrameLayout : FrameLayout{

    private var aspectRatio = 1f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a: TypedArray =
                context
                        .theme
                        .obtainStyledAttributes(
                                attrs,
                                R.styleable.AspectRatioFrameLayout,
                                0,
                                0)

        try {
            aspectRatio = a.getFloat(R.styleable.AspectRatioFrameLayout_aspectRatio, 1f)
        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {

        val a: TypedArray =
                context
                        .theme
                        .obtainStyledAttributes(
                                attrs,
                                R.styleable.AspectRatioFrameLayout,
                                0,
                                0)

        try {
            aspectRatio = a.getFloat(R.styleable.AspectRatioFrameLayout_aspectRatio, 1f)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height =
                MeasureSpec.makeMeasureSpec(
                        (MeasureSpec.getSize(widthMeasureSpec) * aspectRatio).toInt(),
                        View.MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, height)
    }
}