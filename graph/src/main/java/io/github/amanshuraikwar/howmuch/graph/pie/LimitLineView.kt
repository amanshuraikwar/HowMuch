package io.github.amanshuraikwar.howmuch.graph.pie

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
    private val baselinePath = Path()
    private var baselinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var yLimitLineWidth = 6f
    private var yLimitLineColor = Color.LTGRAY
    private val yLimitLinePath = Path()
    private var yLimitLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dashedLineGap = 12f

    private var yLimitTextSize = 40f
    private var yLimitTextColor = Color.DKGRAY
    private var yLimitTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var yLimitTextPadding = 20f

    private var curXTextSize = 40f
    private var curXTextColor = Color.DKGRAY
    private var curXTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var curXTextPadding = 20f

    private var projectionLineWidth = 6f
    private var projectionLineColor = Color.LTGRAY
    private val projectionLinePath = Path()
    private var projectionLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var projectionLineGap = 12f

    private var intersectionPointRadius = 12f
    private var intersectionPointColor = Color.RED
    private var intersectionPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var data: List<Item>? =
            mutableListOf(
                    Item(2f, 100f),
                    Item(5f, 300f),
                    Item(10f, 60f),
                    Item(21f, 400f)
            )

    var xRawMax: Float = 31f
    var yRawMax: Float = 31f
    var xRawCur: Float = 21f
    var yRawLimit: Float = 1000f

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

        graphRect.set(
                contentRect.left,
                contentRect.top + yLimitTextSize + yLimitTextPadding,
                contentRect.right,
                contentRect.bottom - curXTextSize - curXTextPadding
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
                (yRawLimit.scaleY() + yLimitTextPadding).canvasY(),
                yLimitTextPaint
        )

        // draw provided data

        linePath.reset()
        // don't move to exact (0,0), it looks bad
        linePath.moveTo(0f.canvasX(), dashedLineGap.canvasY())

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
                xRawCur.toString(),
                xRawCur.scaleX().canvasX(),
                (0f - curXTextPadding - curXTextSize).canvasY(),
                curXTextPaint
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
                    (0f.scaleY() + projectionLineGap).canvasY()
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

            if (projectedYLimitIntersectionX < xMax - projectionLineGap) {

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
            if (projectedIntersectionY <= (yMax - projectionLineGap)) {

                // draw line to x axis

                linePath.reset()
                linePath.moveTo(
                        xRawCur.scaleX().canvasX(),
                        projectedIntersectionY.canvasY()
                )
                linePath.lineTo(
                        xRawCur.scaleX().canvasX(),
                        (0f.scaleY() + projectionLineGap).canvasY()
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

            if (projectedYLimitIntersectionX < xMax - projectionLineGap) {

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


        a.recycle()

        linePaint.style = Paint.Style.STROKE
        linePaint.color = lineColor
        linePaint.strokeWidth = lineWidth
        linePaint.strokeCap = Paint.Cap.ROUND

        baselinePaint.style = Paint.Style.STROKE
        baselinePaint.color = baselineColor
        baselinePaint.strokeWidth = baselineWidth
        baselinePaint.strokeCap = Paint.Cap.SQUARE

        yLimitLinePaint.style = Paint.Style.STROKE
        yLimitLinePaint.color = yLimitLineColor
        yLimitLinePaint.strokeWidth = yLimitLineWidth
        yLimitLinePaint.pathEffect =
                DashPathEffect(floatArrayOf(dashedLineGap, dashedLineGap), 0f)

        projectionLinePaint.style = Paint.Style.STROKE
        projectionLinePaint.color = projectionLineColor
        projectionLinePaint.strokeWidth = projectionLineWidth
        projectionLinePaint.pathEffect =
                DashPathEffect(floatArrayOf(projectionLineGap, projectionLineGap), 0f)

        yLimitTextPaint.style = Paint.Style.FILL
        yLimitTextPaint.color = yLimitTextColor
        yLimitTextPaint.textSize = yLimitTextSize
        yLimitTextPaint.textAlign =  Paint.Align.LEFT

        curXTextPaint.style = Paint.Style.FILL
        curXTextPaint.color = curXTextColor
        curXTextPaint.textSize = curXTextSize
        curXTextPaint.textAlign =  Paint.Align.CENTER

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