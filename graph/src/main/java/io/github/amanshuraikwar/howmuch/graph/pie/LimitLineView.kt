package io.github.amanshuraikwar.howmuch.graph.pie

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import io.github.amanshuraikwar.howmuch.graph.R

class LimitLineView : View {

    private val contentRect = RectF()
    private val graphRect = RectF()

    private var lineWidth = 6f
    private var lineColor = Color.BLACK
    private val linePath = Path()
    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var baselineWidth = 6f
    private var baselineColor = Color.LTGRAY
    private var baselinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var yLimitLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lineDashGap = 12f

    private var valueTextSize = 40f
    private var valueTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var valueTextPadding = 20f

    private var labelTextSize = 40f
    private var labelTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var labelTextPadding = 20f

    private var projectionLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var intersectionPointRadius = 12f
    private var intersectionPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var data: List<Item>? = null

    var xRawMax: Float = 0f
    var yRawMax: Float = 0f
    var xRawCur: Float = 0f
    var yRawLimit: Float = 0f

    constructor(context: Context): super(context) {
        init(context, null, R.attr.LimitLineViewStyle, R.style.LimitLineView)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs, R.attr.LimitLineViewStyle, R.style.LimitLineView)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, R.style.LimitLineView)
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

        if (data == null) {
            return
        }

        graphRect.set(
                contentRect.left,
                contentRect.top + valueTextSize + valueTextPadding,
                contentRect.right,
                contentRect.bottom - labelTextSize - labelTextPadding
        )

        yRawMax = Math.max(
                yRawLimit,
                data?.sumByDouble { it.y.toDouble() }?.toFloat() ?: return
        )

        val xMax = graphRect.width()
        val yMax = graphRect.height()

        // draw baseline

        linePath.reset()
        linePath.moveTo(0f.canvasX(), 0f.canvasY())
        linePath.lineTo(xMax.canvasX(), 0f.canvasY())
        canvas.drawPath(linePath, baselinePaint)

        // draw y limit line

        linePath.reset()
        linePath.moveTo(0f.canvasX(), yRawLimit.scaleY().canvasY())
        linePath.lineTo(xMax.canvasX(), yRawLimit.scaleY().canvasY())
        canvas.drawPath(linePath, yLimitLinePaint)

        // draw y limit text

        canvas.drawText(
                yRawLimit.toString(),
                0f.canvasX(),
                (yRawLimit.scaleY() + valueTextPadding).canvasY(),
                valueTextPaint
        )

        // draw provided data

        linePath.reset()
        // don't move to exact (0,0), it looks bad
        linePath.moveTo(0f.canvasX(), this.lineDashGap.canvasY())

        var lastDrawnX = 0f
        var lastDrawnY = 0f

        var sum = 0f
        data?.forEach {
            sum += it.y
            linePath.lineTo(
                    it.x.scaleX().canvasX(),
                    sum.scaleY().canvasY()
            )
            lastDrawnX = it.x
            lastDrawnY = sum
        }

        canvas.drawPath(linePath, linePaint)

        // draw cur x text

        canvas.drawText(
                "TODAY",
                xRawCur.scaleX().canvasX(),
                (0f - labelTextPadding - labelTextSize).canvasY(),
                labelTextPaint
        )

        // only draw projections if the current raw max is less that raw limit

        if (yRawMax > yRawLimit) {
            return
        }

        // draw future projections
        //
        // taking line connecting (0,0) and last drawn (x,y)
        // as the proposed projection
        //
        // y = m * x + c
        //
        // finding m

        val m = lastDrawnY.scaleY() / lastDrawnX.scaleX()

        // now there can be two cases
        // 1 -> last xRaw drawn == xRawCur
        // 2 -> last xRaw drawn < xRawCur

        if (lastDrawnX == xRawCur) {

            // draw line to x axis

            linePath.reset()
            linePath.moveTo(
                    lastDrawnX.scaleX().canvasX(),
                    lastDrawnY.scaleY().canvasY()
            )
            linePath.lineTo(
                    lastDrawnX.scaleX().canvasX(),
                    (0f.scaleY() + this.lineDashGap).canvasY()
            )

            canvas.drawPath(linePath, projectionLinePaint)

            // draw projected line

            linePath.reset()
            linePath.moveTo(
                    lastDrawnX.scaleX().canvasX(),
                    lastDrawnY.scaleY().canvasY()
            )

            // if we continue on projected slope
            // what is the projected X and projected Y
            // when the line reaches boundary

            val projectedX = yMax / m
            val projectedY = xMax * m

            // at least one of these points will have to fall inside graph rect space

            if (projectedX <= xMax) {
                linePath.lineTo(projectedX.canvasX(), yMax.canvasY())
            } else {
                linePath.lineTo(xMax.canvasX(), projectedY.canvasY())
            }

            canvas.drawPath(linePath, projectionLinePaint)

            // draw intersection point

            linePath.reset()
            linePath.addCircle(
                    lastDrawnX.scaleX().canvasX(),
                    lastDrawnY.scaleY().canvasY(),
                    intersectionPointRadius,
                    Path.Direction.CW
            )

            canvas.drawPath(linePath, intersectionPointPaint)

            // draw y limit intersection point

            val projectedYLimitIntersectionX = yRawLimit.scaleY() / m

            if (projectedYLimitIntersectionX < xMax - this.lineDashGap) {

                linePath.reset()
                linePath.addCircle(
                        projectedYLimitIntersectionX.canvasX(),
                        yRawLimit.scaleY().canvasY(),
                        intersectionPointRadius,
                        Path.Direction.CW
                )

                canvas.drawPath(linePath, intersectionPointPaint)

            }

        } else {

            // draw projected line

            linePath.reset()
            linePath.moveTo(
                    lastDrawnX.scaleX().canvasX(),
                    lastDrawnY.scaleY().canvasY()
            )

            // if we continue on projected slope
            // what is the projected X and projected Y
            // when the line reaches boundary

            val projectedX = yMax / m
            val projectedY = xMax * m

            // at least one of these points will have to fall inside graph rect space

            if (projectedX <= xMax) {
                linePath.lineTo(projectedX.canvasX(), yMax.canvasY())
            } else {
                linePath.lineTo(xMax.canvasX(), projectedY.canvasY())
            }

            canvas.drawPath(linePath, projectionLinePaint)

            // to draw projected line to cur raw X
            // there can be two cases
            // either the intersection with the projected line
            // lies within the graph rect or not

            val projectedIntersectionY = xRawCur.scaleX() * m

            // only draw if it falls in the graph rect area
            if (projectedIntersectionY <= (yMax - this.lineDashGap)) {

                // draw line to x axis

                linePath.reset()
                linePath.moveTo(
                        xRawCur.scaleX().canvasX(),
                        projectedIntersectionY.canvasY()
                )
                linePath.lineTo(
                        xRawCur.scaleX().canvasX(),
                        (0f.scaleY() + this.lineDashGap).canvasY()
                )

                canvas.drawPath(linePath, projectionLinePaint)

                // draw intersection point

                linePath.reset()
                linePath.addCircle(
                        xRawCur.scaleX().canvasX(),
                        projectedIntersectionY.canvasY(),
                        intersectionPointRadius,
                        Path.Direction.CW
                )

                canvas.drawPath(linePath, intersectionPointPaint)

            }

            // draw y limit intersection point

            val projectedYLimitIntersectionX = yRawLimit.scaleY() / m

            if (projectedYLimitIntersectionX < xMax - this.lineDashGap) {

                linePath.reset()
                linePath.addCircle(
                        projectedYLimitIntersectionX.canvasX(),
                        yRawLimit.scaleY().canvasY(),
                        intersectionPointRadius,
                        Path.Direction.CW
                )

                canvas.drawPath(linePath, intersectionPointPaint)

            }
        }
    }

    private fun Float.scaleX():Float {
        return this * graphRect.width() / xRawMax
    }

    private fun Float.scaleY():Float {
        return this * graphRect.height() / yRawMax
    }

    private fun Float.canvasX():Float {
        return graphRect.left + this
    }

    private fun Float.canvasY():Float {
        return graphRect.bottom - this
    }


    private fun init(context: Context,
                     attrs: AttributeSet?,
                     defStyleAttr: Int,
                     defStyleRes: Int) {

        val a =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.LimitLineView,
                        defStyleAttr,
                        defStyleRes
                )

        lineWidth = a.getDimension(R.styleable.LimitLineView_llv_lineWidth, 0f)
        lineColor = a.getColor(R.styleable.LimitLineView_llv_lineColor, 0)

        baselineWidth = a.getDimension(R.styleable.LimitLineView_llv_baselineWidth, 0f)
        baselineColor = a.getColor(R.styleable.LimitLineView_llv_baselineColor, 0)

        lineDashGap = a.getDimension(R.styleable.LimitLineView_llv_lineDashGap, 0f)

        intersectionPointRadius = a.getDimension(R.styleable.LimitLineView_llv_intersectionPointRadius, 0f)
        val intersectionPointColor = a.getColor(R.styleable.LimitLineView_llv_intersectionPointColor, 0)

        valueTextSize = a.getDimension(R.styleable.LimitLineView_llv_valueTextSize, 0f)
        valueTextPadding = a.getDimension(R.styleable.LimitLineView_llv_valuePadding, 0f)

        val valueTextColor = a.getColor(R.styleable.LimitLineView_llv_valueTextColor, 0)
        val valueFontFamilyResId = a.getResourceId(R.styleable.LimitLineView_llv_valueFontFamily, 0)
        val valueTextStyle = a.getInt(R.styleable.LimitLineView_llv_valueTextStyle, -1)

        labelTextSize = a.getDimension(R.styleable.LimitLineView_llv_labelTextSize, 0f)
        labelTextPadding = a.getDimension(R.styleable.LimitLineView_llv_labelPadding, 0f)

        val labelTextColor = a.getColor(R.styleable.LimitLineView_llv_labelTextColor, 0)
        val labelFontFamilyResId = a.getResourceId(R.styleable.LimitLineView_llv_labelFontFamily, 0)
        val labelTextStyle = a.getInt(R.styleable.LimitLineView_llv_labelTextStyle, -1)

        a.recycle()

        linePaint.style = Paint.Style.STROKE
        linePaint.color = lineColor
        linePaint.strokeWidth = lineWidth
        linePaint.strokeCap = Paint.Cap.ROUND

        baselinePaint.style = Paint.Style.STROKE
        baselinePaint.color = baselineColor
        baselinePaint.strokeWidth = baselineWidth
        baselinePaint.strokeCap = Paint.Cap.ROUND
        baselinePaint.pathEffect =
                DashPathEffect(floatArrayOf(lineDashGap, lineDashGap), 0f)

        yLimitLinePaint.style = Paint.Style.STROKE
        yLimitLinePaint.color = baselineColor
        yLimitLinePaint.strokeWidth = baselineWidth
        yLimitLinePaint.pathEffect =
                DashPathEffect(floatArrayOf(lineDashGap, lineDashGap), 0f)

        projectionLinePaint.style = Paint.Style.STROKE
        projectionLinePaint.color = baselineColor
        projectionLinePaint.strokeWidth = baselineWidth
        projectionLinePaint.pathEffect =
                DashPathEffect(floatArrayOf(lineDashGap, lineDashGap), 0f)

        valueTextPaint.style = Paint.Style.FILL
        valueTextPaint.color = valueTextColor
        valueTextPaint.textSize = valueTextSize
        valueTextPaint.textAlign =  Paint.Align.LEFT

        var valueNormalTypeface =
                if (valueFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, valueFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (valueTextStyle == 0) {
            valueNormalTypeface = Typeface.create(valueNormalTypeface, Typeface.BOLD)
        }

        valueTextPaint.typeface = valueNormalTypeface

        labelTextPaint.style = Paint.Style.FILL
        labelTextPaint.color = labelTextColor
        labelTextPaint.textSize = labelTextSize
        labelTextPaint.textAlign =  Paint.Align.CENTER

        var labelNormalTypeface =
                if (labelFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, labelFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (labelTextStyle == 0) {
            labelNormalTypeface = Typeface.create(labelNormalTypeface, Typeface.BOLD)
        }

        labelTextPaint.typeface = labelNormalTypeface

        intersectionPointPaint.style = Paint.Style.FILL
        intersectionPointPaint.color = intersectionPointColor
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

    data class Item(val x: Float,
                    val y: Float)
}