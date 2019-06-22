package io.github.amanshuraikwar.howmuch.graph.pie

import android.content.Context
import android.graphics.*
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.create
import android.util.AttributeSet
import android.view.View
import io.github.amanshuraikwar.howmuch.graph.R

import androidx.core.content.res.ResourcesCompat

class BarView : View {

    private val contentRect = RectF()
    private val sparkPath = Path()
    private var lineWidth = 0f

    private var lineColor = Color.BLACK
    private var lineBackgroundColor = Color.LTGRAY

    private var sparkLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lineBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var labelTextSize = 40f
    private var labelPadding = 20f

    var data: List<BarItem>? = null

    constructor(context: Context): super(context) {
        init(context, null, R.attr.BarViewStyle, R.style.BarView)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs, R.attr.BarViewStyle, R.style.BarView)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, R.style.BarView)
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

            val max = data.maxBy { it.value }!!

            val gap = contentRect.width() / (data.size - 1)

            var i = 0

            data.forEach {

                sparkPath.reset()

                val x = contentRect.left + i * gap
                val y =
                        (it.value.div(max.value))
                                .times(contentRect.height() - labelTextSize - labelPadding)

                sparkPath.moveTo(x, contentRect.bottom - labelTextSize - labelPadding)
                sparkPath.lineTo(x, contentRect.top)
                canvas.drawPath(sparkPath, lineBgPaint)

                sparkPath.reset()

                sparkPath.moveTo(x, contentRect.bottom - labelTextSize - labelPadding)
                sparkPath.lineTo(x, contentRect.bottom - labelTextSize - labelPadding - y)
                canvas.drawPath(sparkPath, sparkLinePaint)

                canvas.drawText(it.label, x, contentRect.bottom, labelPaint)

                i++
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
                        R.styleable.BarView,
                        defStyleAttr,
                        defStyleRes
                )

        lineWidth = a.getDimension(R.styleable.BarView_bv_lineWidth, lineWidth)
        lineColor = a.getColor(R.styleable.BarView_bv_lineColor, lineColor)
        lineBackgroundColor = a.getColor(R.styleable.BarView_bv_lineBackground, lineBackgroundColor)
        labelTextSize = a.getDimension(R.styleable.BarView_bv_labelTextSize, labelTextSize)
        labelPadding = a.getDimension(R.styleable.BarView_bv_labelPadding, labelPadding)
        val fontFamilyResId = a.getResourceId(R.styleable.BarView_bv_labelFontFamily, 0)
        val labelTextColor = a.getColor(R.styleable.BarView_bv_labelTextColor, Color.BLACK)
        val labelTextStyle = a.getInt(R.styleable.BarView_bv_labelTextStyle, -1)
        a.recycle()

        sparkLinePaint.style = Paint.Style.STROKE
        sparkLinePaint.color = lineColor
        sparkLinePaint.strokeWidth = lineWidth
        sparkLinePaint.strokeCap = Paint.Cap.ROUND

        lineBgPaint.style = Paint.Style.STROKE
        lineBgPaint.color = lineBackgroundColor
        lineBgPaint.strokeWidth = lineWidth
        lineBgPaint.strokeCap = Paint.Cap.ROUND

        labelPaint.style = Paint.Style.FILL
        labelPaint.color = labelTextColor
        labelPaint.textSize = labelTextSize
        labelPaint.textAlign =  Paint.Align.CENTER

        var normalTypeface =
                if (fontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, fontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (labelTextStyle == 0) {
            normalTypeface = create(normalTypeface, BOLD)
        }

        labelPaint.typeface = normalTypeface
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

    data class BarItem(val label: String,
                       val value: Float)
}