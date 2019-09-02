package io.github.amanshuraikwar.howmuch.graph

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.create
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils

import androidx.core.content.res.ResourcesCompat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BarView2 @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.BarView2Style,
        defStyleRes: Int = R.style.BarView2
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val contentRect = RectF()
    private val graphRect = RectF()

    private var lineWidth = 0f

    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lineColor = Color.BLACK
    private val linePath = Path()

    private var lineBackgroundColor = Color.LTGRAY
    private var lineBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lineBackgroundPath = Path()

    private var labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var labelTextColor = Color.BLACK

    private var labelTextSize = 40f
    private var labelPadding = 20f

    private var clickedBgColor = Color.parseColor("#eeE8F5E9")
    private val clickedBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val clickedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var clickedBarValueTextColor = lineColor
    private var clickedBarValueTextSize = 40f
    private val clickedBarValueTextPaint =  Paint(Paint.ANTI_ALIAS_FLAG)

    private var maxValue = 0f

    /**
     * Diagonal length of the view.
     */
    private var diagonal = 0

    /**
     * Used generically for calculating text bounds
     */
    private val textBounds = Rect()

    private var touchedX = 0f
    private var touchedY = 0f

    private var checked = false
    private var progressAnimator: ValueAnimator? = null
    private var progress = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private var clickedDataIndex = 0

    var data: List<BarItem> =
            mutableListOf(
                    BarItem("MON", 0f),
                    BarItem("TUE", 0f),
                    BarItem("WED", 0f),
                    BarItem("THU", 0f),
                    BarItem("FRI", 0f),
                    BarItem("SAT", 0f),
                    BarItem("SUN", 0f)
            )
        set(value) {
            field = value
            updateGraphRect()
            populatePath()
        }

    init {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        diagonal = sqrt((w*w + h*h).toDouble()).toInt()
        updateContentRect()
        updateGraphRect()
        populatePath()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        linePaint.color = lineColor
        lineBackgroundPaint.color = lineBackgroundColor
        labelPaint.color = labelTextColor

        canvas.drawPath(lineBackgroundPath, lineBackgroundPaint)
        canvas.drawPath(linePath, linePaint)

        data.forEachIndexed { index, barItem ->

            val x = (index + 1).getGraphX()

            labelPaint.getTextBounds(
                    barItem.label, 0, barItem.label.length, textBounds)

            canvas.drawText(
                    barItem.label,
                    x,
                    contentRect.bottom
                            - (contentRect.bottom - (graphRect.bottom + labelPadding))/2
                            - (textBounds.bottom + textBounds.top)/2.0f,
                    labelPaint
            )
        }

        if (progress != 0f) {

            canvas.drawCircle(
                    touchedX,
                    touchedY,
                    progress * diagonal,
                    clickedBgPaint
            )

            clickedBarPaint.strokeWidth =
                    max(lineWidth, lineWidth + (progress - 0.8f) * labelPadding)

            canvas.drawLine(
                    (clickedDataIndex+1).getGraphX(),
                    max(0f.getGraphY(), 0f.getGraphY() + (progress - 0.8f)*labelPadding),
                    (clickedDataIndex+1).getGraphX(),
                    min(data[clickedDataIndex].value.getGraphY(),
                            (data[clickedDataIndex].value).getGraphY()
                                    - (progress - 0.5f)*labelPadding),
                    clickedBarPaint
            )
        }

        if (progress > 0.5f) {
            drawClickedBarValue(canvas)
        }
    }

    private fun drawClickedBarValue(canvas: Canvas) {

        var x = (clickedDataIndex+1).getGraphX()

        clickedBarValueTextPaint.textAlign =
                if (x >= contentRect.centerX()) {
                    Paint.Align.RIGHT
                } else {
                    Paint.Align.LEFT
                }

        x = if (x >= contentRect.centerX()) {
            x - lineWidth - (progress + 0.5f) * 40f
        } else {
            x + lineWidth + (progress + 0.5f) * 40f
        }

        val valueY = data[clickedDataIndex].value.getGraphY()

        val y =
                if (valueY >= contentRect.centerY()) {
                    valueY - clickedBarValueTextPaint.textSize/2
                } else {
                    valueY + clickedBarValueTextPaint.textSize/2
                }

        clickedBarValueTextPaint.getTextBounds(
                data[clickedDataIndex].value.toInt().toString(),
                0,
                data[clickedDataIndex].value.toInt().toString().length,
                textBounds
        )

        clickedBarValueTextPaint.color =
                Color.argb(
                        (255*progress).toInt(),
                        Color.red(clickedBarValueTextColor),
                        Color.green(clickedBarValueTextColor),
                        Color.blue(clickedBarValueTextColor)
                )

        canvas.drawText(
                data[clickedDataIndex].value.toInt().toString(),
                x,
                y - (textBounds.bottom + textBounds.top) / 2.0f,
                clickedBarValueTextPaint
        )
    }

    private fun init(context: Context,
                     attrs: AttributeSet?,
                     defStyleAttr: Int,
                     defStyleRes: Int) {

        val a =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.BarView2,
                        defStyleAttr,
                        defStyleRes
                )

        lineWidth = a.getDimension(R.styleable.BarView2_bv2_lineWidth, lineWidth)

        lineColor = a.getColor(R.styleable.BarView2_bv2_lineColor, lineColor)
        lineBackgroundColor =
                a.getColor(R.styleable.BarView2_bv2_lineBackground, lineBackgroundColor)

        labelTextSize = a.getDimension(R.styleable.BarView2_bv2_labelTextSize, labelTextSize)
        labelPadding = a.getDimension(R.styleable.BarView2_bv2_labelPadding, labelPadding)

        val fontFamilyResId =
                a.getResourceId(R.styleable.BarView2_bv2_labelFontFamily, 0)
        labelTextColor = a.getColor(R.styleable.BarView2_bv2_labelTextColor, Color.BLACK)
        val labelTextStyle = a.getInt(R.styleable.BarView2_bv2_labelTextStyle, -1)

        linePaint.style = Paint.Style.STROKE
        linePaint.color = lineColor
        linePaint.strokeWidth = lineWidth
        linePaint.strokeCap = Paint.Cap.ROUND

        clickedBarPaint.color = lineColor

        lineBackgroundPaint.style = Paint.Style.STROKE
        lineBackgroundPaint.color = lineBackgroundColor
        lineBackgroundPaint.strokeWidth = lineWidth
        lineBackgroundPaint.strokeCap = Paint.Cap.ROUND

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

        clickedBarPaint.apply {
            style = Paint.Style.STROKE
            color = lineColor
            strokeWidth = lineWidth
            strokeCap = Paint.Cap.ROUND
        }

        clickedBgColor = a.getColor(R.styleable.BarView2_bv2_clickedBgColor, clickedBgColor)
        clickedBgPaint.run {
            style = Paint.Style.FILL
            color = clickedBgColor
        }

        clickedBarValueTextColor =
                a.getColor(
                        R.styleable.BarView2_bv2_clickedBarValueTextColor,
                        clickedBarValueTextColor)

        clickedBarValueTextSize =
                a.getDimension(
                        R.styleable.BarView2_bv2_clickedBarValueTextSize,
                        clickedBarValueTextSize)

        val clickedBarValueTextStyle =
                a.getInt(R.styleable.BarView2_bv2_clickedBarValueTextStyle, -1)

        val clickedBarValueTextFontFamilyResId =
                a.getResourceId(
                        R.styleable.BarView2_bv2_clickedBarValueTextFontFamily, 0)

        var clickedBarValueTextTypeface =
                if (clickedBarValueTextFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, clickedBarValueTextFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (clickedBarValueTextStyle == 0) {
            clickedBarValueTextTypeface = create(clickedBarValueTextTypeface, BOLD)
        }

        clickedBarValueTextPaint.run {
            style = Paint.Style.FILL
            textSize = clickedBarValueTextSize
            color = clickedBarValueTextColor
            typeface = clickedBarValueTextTypeface
        }

        a.recycle()

        setOnTouchListener { v, event ->
            if (progress == 0f) {
                touchedX = event.x
                touchedY = event.y
            }
            false
        }

        setOnClickListener {
            if (progress == 0f) {
                clickedDataIndex = touchedX.getDataIndex()
            }
            toggle()
        }
    }

    private fun toggle() {

        val newProgress = if (checked) 0f else 1f

        checked = !checked

        if (newProgress != progress) {
            progressAnimator?.cancel()
            val interpolator =
                    AnimationUtils
                            .loadInterpolator(context, android.R.interpolator.fast_out_slow_in)
            progressAnimator =
                    ValueAnimator.ofFloat(progress, newProgress).apply {
                        addUpdateListener {
                            progress = it.animatedValue as Float
                        }
                        this.interpolator = interpolator
                        duration = if (checked) 350L else 200L
                        start()
                    }
        }
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
        progress = if (this.checked) {
            1f
        } else {
            0f
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateContentRect()
        updateGraphRect()
        populatePath()
        invalidate()
    }

    private fun populatePath() {

        maxValue = data.maxBy { it.value }?.value ?: 0f

        linePath.reset()
        lineBackgroundPath.reset()

        data.forEachIndexed { index, barItem ->

            val x = (index + 1).getGraphX()
            val y = barItem.value.getGraphY()

            linePath.moveTo(x, 0f.getGraphY())
            linePath.lineTo(x, y)

            lineBackgroundPath.moveTo(x, 0f.getGraphY())
            lineBackgroundPath.lineTo(x, maxValue.getGraphY())
        }
    }

    private fun updateGraphRect() {
        graphRect.set(
                contentRect.left,
                contentRect.top + labelPadding,
                contentRect.right,
                contentRect.bottom - labelTextSize - labelPadding
        )
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

    /**
     * Converts value to y coordinate on canvas.
     */
    private fun Float.getGraphY() = graphRect.bottom - (this/maxValue) * graphRect.height()

    private fun Int.getGraphX()
            = graphRect.left + (this) * (graphRect.width() / (data.size + 1))

    private fun Float.getDataIndex(): Int {

        var index =
                ((this - graphRect.left) * ((data.size + 1) / graphRect.width())).roundToInt() - 1

        if (index < 0) {
            index = 0
        } else if (index >= data.size) {
            index = data.size - 1
        }

        return index
    }

    data class BarItem(val label: String,
                       val value: Float)
}