package io.github.amanshuraikwar.howmuch.graph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class PieView : View {

    private val contentRect = RectF()
    private val sparkPath = Path()
    private var lineWidth = 0f

    // gap arc length
    private var gap = 2f

    // gap angle in degrees
    private var gapAngle = 0f

    private var lineBackgroundColor = Color.LTGRAY

    private var sparkLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lineBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var data: List<PieItem>? = null

    constructor(context: Context): super(context) {
        init(context, null, R.attr.PieViewStyle, R.style.spark_PieView)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs, R.attr.PieViewStyle, R.style.spark_PieView)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, R.style.spark_PieView)
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

        data?.let {

            data ->

            // getting total sum of values
            val total = data.sumByFloat { it.value }

            // scale the values to 0 -> 360
            val scaled = data.map { it.newValue(360 * it.value / total) }

            var startAngle = 0f

            var nothingDrawn = true

            scaled.forEach {

                // skip drawing if value is zero
                if (it.value > 0f) {

                    nothingDrawn = false

                    sparkPath.reset()

                    // if value is 360 then don't add gap
                    if (it.value == 360f) {
                        sparkPath.addArc(contentRect, startAngle, it.value)
                    } else {
                        sparkPath.addArc(contentRect, startAngle, it.value - gapAngle)
                    }

                    sparkLinePaint.color = it.color
                    canvas.drawPath(sparkPath, sparkLinePaint)
                    startAngle += it.value
                }
            }

            if (nothingDrawn) {
                sparkPath.reset()
                sparkPath.addArc(contentRect, 0f, 360f)
                canvas.drawPath(sparkPath, lineBgPaint)
            }
        }
    }

    private fun init(context: Context,
                     attrs: AttributeSet?,
                     defStyleAttr: Int,
                     defStyleRes: Int) {

        val a =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.PieView,
                        defStyleAttr,
                        defStyleRes
                )

        lineWidth = a.getDimension(R.styleable.PieView_pie_lineWidth, lineWidth)
        gap = a.getDimension(R.styleable.PieView_pie_gap, gap)
        lineBackgroundColor = a.getColor(R.styleable.PieView_pie_lineBackground, lineBackgroundColor)

        a.recycle()

        sparkLinePaint.style = Paint.Style.STROKE
        sparkLinePaint.color = Color.BLUE
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

        // finding angle, given gap arc length

        // theta = s / r
        val angle = gap / (contentRect.width() / 2)

        // approx conversion of radian to degree
        val theta = angle * 180 / 3.14

        gapAngle = theta.toFloat()
    }

    data class PieItem(val label: String,
                       val value: Float,
                       @ColorInt val color: Int)

    private fun PieItem.newValue(newValue: Float): PieItem {
        return PieItem(this.label, newValue, color)
    }

    private fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
        var sum = 0f
        for (element in this) {
            sum += selector(element)
        }
        return sum
    }
}