package io.github.amanshuraikwar.howmuch.graph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import io.github.amanshuraikwar.howmuch.graph.R
import kotlin.math.min

class VerticalProgressBar : View {

    private val contentRect = RectF()
    private val sparkPath = Path()
    private var lineWidth = 0f

    var lineColor = Color.BLACK
    var lineBackgroundColor = Color.LTGRAY

    private var sparkLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lineBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var progress: Int = 50
    var max: Int = 100

    var orientation = 1

    constructor(context: Context): super(context) {
        init(context, null, R.attr.VerticalProgressBarStyle, R.style.VerticalProgressBar)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs, R.attr.VerticalProgressBarStyle, R.style.VerticalProgressBar)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, R.style.VerticalProgressBar)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        updateContentRect()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        lineBgPaint.color = lineBackgroundColor

        sparkPath.reset()

        if (orientation == 1) {

            sparkPath.moveTo(contentRect.centerX(), contentRect.top)
            sparkPath.lineTo(contentRect.centerX(), contentRect.bottom)
            canvas.drawPath(sparkPath, lineBgPaint)

            sparkPath.reset()
            sparkPath.moveTo(contentRect.centerX(), contentRect.bottom)

            val scaledProgress = min(progress, max) * contentRect.height() / max

            sparkPath.lineTo(contentRect.centerX(), contentRect.bottom - scaledProgress)

        } else {

            sparkPath.moveTo(contentRect.left, contentRect.centerY())
            sparkPath.lineTo(contentRect.right, contentRect.centerY())
            canvas.drawPath(sparkPath, lineBgPaint)

            sparkPath.reset()
            sparkPath.moveTo(contentRect.left, contentRect.centerY())

            val scaledProgress = min(progress, max) * contentRect.width() / max

            sparkPath.lineTo(contentRect.left + scaledProgress, contentRect.centerY())

        }

        sparkLinePaint.color = lineColor
        canvas.drawPath(sparkPath, sparkLinePaint)

    }

    private fun init(context: Context,
                     attrs: AttributeSet?,
                     defStyleAttr: Int,
                     defStyleRes: Int) {

        val a =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.VerticalProgressBar,
                        defStyleAttr,
                        defStyleRes
                )

        lineWidth = a.getDimension(R.styleable.VerticalProgressBar_vpb_lineWidth, lineWidth)
        lineColor = a.getColor(R.styleable.VerticalProgressBar_vpb_lineColor, lineColor)
        lineBackgroundColor = a.getColor(R.styleable.VerticalProgressBar_vpb_lineBackground, lineBackgroundColor)
        orientation = a.getInt(R.styleable.VerticalProgressBar_vpb_orientation, 1)

        a.recycle()

        sparkLinePaint.style = Paint.Style.STROKE
        sparkLinePaint.color = lineColor
        sparkLinePaint.strokeWidth = lineWidth
        sparkLinePaint.strokeCap = Paint.Cap.ROUND

        lineBgPaint.style = Paint.Style.STROKE
        lineBgPaint.color = lineBackgroundColor
        lineBgPaint.strokeWidth = lineWidth
        lineBgPaint.strokeCap = Paint.Cap.ROUND
    }

    /**
     * Gets the rect representing the 'content area' of the view. This is essentially the bounding
     * rect minus any padding.
     */
    private fun updateContentRect() {
        contentRect.set(
                paddingStart.toFloat(),
                paddingTop.toFloat(),
                (width - paddingEnd).toFloat(),
                (height - paddingBottom).toFloat()
        )
    }
}