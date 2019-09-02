package io.github.amanshuraikwar.howmuch.graph

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Checkable
import androidx.core.content.res.ResourcesCompat
import kotlin.math.max
import kotlin.math.sqrt

class BudgetLineView : View, Checkable {

    /**
     * The rect where the actual stuff is drawn.
     */
    private val contentRect = RectF()

    /**
     * The rect in which the line graph will be drawn
     */
    private val graphRect = RectF()

    private var spentLineWidth = 6f
    private var spentLineColor = Color.BLACK
    private val spentLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val spentLinePath = Path()

    private var baselineWidth = 6f
    private var baselineColor = Color.BLACK
    private val baselinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val baselinePath = Path()

    private var budgetLimitLineWidth = 6f
    private var budgetLimitLineColor = Color.BLACK
    private val budgetLimitLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val budgetLimitLinePath = Path()

    private var budgetLimitShiftColor = Color.BLACK
    private val budgetLimitShiftPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val budgetLimitShiftPath = Path()

    private var budgetLimitTextSize = 40f
    private var budgetLimitTextColor = Color.BLACK
    private val budgetLimitTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var budgetLimitTextPadding = 20f

    private var budgetProjectionLineWidth = 6f
    private var budgetProjectionLineColor = Color.BLACK
    private val budgetProjectionLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val budgetProjectionLinePath = Path()

    private var dateProjectionLineWidth = 6f
    private var dateProjectionLineColor = Color.BLACK
    private val dateProjectionLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dateProjectionLinePath = Path()

    private var zeroOffset = 6f

    private var dateTextSize = 40f
    private var dateTextColor = Color.WHITE
    private val dateTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var dateRectPadding = 40f
    private var dateRectColor = Color.BLACK
    private val dateRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dateRectRadius = 6f
    private val dateRectPath = Path()

    private var intersectionPointRadius = 12f
    private var intersectionPointColor = Color.WHITE
    private val intersectionPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val intersectionPointPath = Path()

    private var intersectionPointBorderWidth = 6f
    private var intersectionPointBorderColor = Color.BLACK
    private val intersectionPointBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var intersectionPointTextBgColor = Color.parseColor("#eeFFEBEE")
    private val intersectionPointTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var touchedPointAmountTextSize = 120f
    private var touchedPointAmountTextColor = Color.RED
    private val touchedPointAmountTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var touchedPointAmountTextPadding = 20f

    var data: List<Item> = mutableListOf<Item>().apply {
        for (i in 1..31) this.add(Item(i, i * 10f))
    }
    set(value) {
        field = value
        populatePath()
        //invalidate()
    }

    var budgetLimit = 1000f
    set(value) {
        field = value
        //invalidate()
    }

    /**
     * Shifted budget limit if budgetLimitReachDate != floor(budgetLimitReachDate)
     */
    private var budgetLimitShifted = budgetLimit

    /**
     * (Projected or Actual) date at which the budget limit (will reach or have reached).
     */
    private var budgetLimitReachDate = 0

    var maxDate = 31
        set(value) {
            field = value
            //invalidate()
        }

    var today = 9
        set(value) {
            field = if (value <= 0) 1 else if (value > maxDate) maxDate else value
            //invalidate()
        }

    var todayLabel = "TODAY"
        set(value) {
            field = value
            //invalidate()
        }

    var puckSize = 8f
        set(value) {
            field = value
            //invalidate()
        }

    var puckGap = 8f
        set(value) {
            field = value
            //invalidate()
        }

    var lastDrawnX = 0f
    var lastDrawnY = 0f

    var isCurMonth = true

    private var todayRect = RectF()
    private var budgetLimitReachDateRect = RectF()

    private var touchedX = 0f
    private var touchedY = 0f

    private var checkedX = false
    private var progressAnimator: ValueAnimator? = null
    private var progress = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private var intersectionPoints = mutableListOf<PointF>()

    /**
     * Diagonal length of the view.
     */
    private var diagonal = 0

    /**
     * Used generically for calculating text bounds
     */
    private val textBounds = Rect()

    constructor(context: Context): super(context) {
        init(context, null, R.attr.BudgetLineViewStyle, R.style.BudgetLineView)
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context, attrs, R.attr.BudgetLineViewStyle, R.style.BudgetLineView)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, R.style.BudgetLineView)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        diagonal = sqrt((w*w + h*h).toDouble()).toInt()
        updateContentRect()
        populatePath()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(baselinePath, baselinePaint)

        canvas.drawPath(budgetLimitShiftPath, budgetLimitShiftPaint)
        canvas.drawPath(budgetLimitLinePath, budgetLimitLinePaint)

        // date projection lines should not be over spent line
        canvas.drawPath(dateProjectionLinePath, dateProjectionLinePaint)

        canvas.drawText(
                budgetLimit.toInt().toString(), graphRect.left,
                budgetLimitShifted.getGraphY() - budgetLimitTextPadding,
                budgetLimitTextPaint)

        canvas.drawPath(dateRectPath, dateRectPaint)
        drawDates(canvas)

        canvas.drawPath(spentLinePath, spentLinePaint)
        canvas.drawPath(budgetProjectionLinePath, budgetProjectionLinePaint)

        canvas.drawPath(intersectionPointPath, intersectionPointPaint)
        canvas.drawPath(intersectionPointPath, intersectionPointBorderPaint)

        if (progress != 0f) {

            canvas.drawCircle(
                    touchedX,
                    touchedY,
                    progress * diagonal,
                    intersectionPointTextBgPaint
            )

            canvas.drawCircle(
                    touchedX,
                    touchedY,
                    max(
                            intersectionPointRadius.toDouble(),
                            (progress + 0.5) * intersectionPointRadius
                    ).toFloat(),
                    intersectionPointPaint
            )

            canvas.drawCircle(
                    touchedX,
                    touchedY,
                    max(
                            intersectionPointRadius.toDouble(),
                            (progress + 0.5) * intersectionPointRadius
                    ).toFloat(),
                    intersectionPointBorderPaint
            )
        }

        if (progress > 0.5f) {
            drawTouchedPointAmountText(canvas)
        }
    }

    private fun drawTouchedPointAmountText(canvas: Canvas) {

        touchedPointAmountTextPaint.textAlign =
                if (touchedX >= contentRect.centerX()) {
                    Paint.Align.RIGHT
                } else {
                    Paint.Align.LEFT
                }

        val amount = touchedY.getAmount().toInt()

        val x = touchedX
        val y =
                if (touchedY >= contentRect.centerY()) {
                    touchedY - (progress + 0.5f) * intersectionPointRadius - touchedPointAmountTextPadding - touchedPointAmountTextPaint.textSize/2
                } else {
                    touchedY + (progress + 0.5f) * intersectionPointRadius + touchedPointAmountTextPadding + touchedPointAmountTextPaint.textSize/2
                }

        touchedPointAmountTextPaint.getTextBounds(
                amount.toString(), 0, amount.toString().length, textBounds)

        touchedPointAmountTextPaint.color =
                Color.argb(
                        (255*progress).toInt(),
                        Color.red(touchedPointAmountTextColor),
                        Color.green(touchedPointAmountTextColor),
                        Color.blue(touchedPointAmountTextColor)
                )

        canvas.drawText(
                amount.toString(),
                x,
                y - (textBounds.bottom + textBounds.top) / 2.0f,
                touchedPointAmountTextPaint
        )
    }

    private fun drawDates(canvas: Canvas) {

        if (today < budgetLimitReachDate) {

            dateTextPaint.textAlign = Paint.Align.RIGHT

            dateTextPaint.getTextBounds(todayLabel, 0, todayLabel.length, textBounds)

            canvas.drawText(
                    todayLabel,
                    todayRect.right - dateRectPadding,
                    todayRect.centerY() - (textBounds.bottom + textBounds.top) / 2.0f,
                    dateTextPaint
            )

            dateTextPaint.textAlign = Paint.Align.LEFT

            dateTextPaint.getTextBounds(
                    budgetLimitReachDate.toString(),
                    0,
                    budgetLimitReachDate.toString().length,
                    textBounds
            )

            canvas.drawText(
                    budgetLimitReachDate.toString(),
                    budgetLimitReachDateRect.left + dateRectPadding,
                    budgetLimitReachDateRect.centerY()
                            - (textBounds.bottom + textBounds.top) / 2.0f,
                    dateTextPaint
            )

        } else if (today == budgetLimitReachDate) {

            dateTextPaint.textAlign = Paint.Align.CENTER

            dateTextPaint.getTextBounds(todayLabel, 0, todayLabel.length, textBounds)

            canvas.drawText(
                    todayLabel,
                    todayRect.centerX(),
                    todayRect.centerY() - (textBounds.bottom + textBounds.top) / 2.0f,
                    dateTextPaint
            )

        } else {

            dateTextPaint.textAlign = Paint.Align.RIGHT

            dateTextPaint.getTextBounds(
                    budgetLimitReachDate.toString(),
                    0,
                    budgetLimitReachDate.toString().length,
                    textBounds
            )

            canvas.drawText(
                    budgetLimitReachDate.toString(),
                    budgetLimitReachDateRect.right - dateRectPadding,
                    budgetLimitReachDateRect.centerY()
                            - (textBounds.bottom + textBounds.top) / 2.0f,
                    dateTextPaint
            )

            dateTextPaint.textAlign = Paint.Align.LEFT

            dateTextPaint.getTextBounds(
                    todayLabel,
                    0,
                    todayLabel.length,
                    textBounds
            )

            canvas.drawText(
                    todayLabel,
                    todayRect.left + dateRectPadding,
                    todayRect.centerY()
                            - (textBounds.bottom + textBounds.top) / 2.0f,
                    dateTextPaint
            )
        }
    }

    private fun init(context: Context,
                     attrs: AttributeSet?,
                     defStyleAttr: Int,
                     defStyleRes: Int) {

        val a =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.BudgetLineView,
                        defStyleAttr,
                        defStyleRes
                )

        spentLineWidth =
                a.getDimension(R.styleable.BudgetLineView_blv_spentLineWidth, spentLineWidth)

        spentLineColor =
                a.getColor(R.styleable.BudgetLineView_blv_spentLineColor, spentLineColor)

        spentLinePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = spentLineWidth
            strokeCap = Paint.Cap.ROUND
            color = spentLineColor
        }

        baselineWidth =
                a.getDimension(R.styleable.BudgetLineView_blv_baselineWidth, baselineWidth)

        baselineColor =
                a.getColor(R.styleable.BudgetLineView_blv_baselineColor, baselineColor)

        baselinePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = baselineWidth
            strokeCap = Paint.Cap.ROUND
            color = baselineColor
        }

        budgetLimitLineWidth =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_budgetLimitLineWidth, budgetLimitLineWidth)

        budgetLimitLineColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_budgetLimitLineColor, budgetLimitLineColor)

        budgetLimitLinePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = budgetLimitLineWidth
            strokeCap = Paint.Cap.ROUND
            color = budgetLimitLineColor
        }

        budgetLimitShiftColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_budgetLimitShiftColor, budgetLimitShiftColor)

        budgetLimitShiftPaint.run {
            style = Paint.Style.FILL
            color = budgetLimitShiftColor
        }

        budgetLimitTextSize =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_budgetLimitTextSize, budgetLimitTextSize)

        budgetLimitTextColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_budgetLimitTextColor, budgetLimitTextColor)

        val budgetLimitTextStyle =
                a.getInt(R.styleable.BudgetLineView_blv_budgetLimitTextStyle, -1)

        val budgetLimitTextFontFamilyResId =
                a.getResourceId(R.styleable.BudgetLineView_blv_budgetLimitTextFontFamily, 0)

        var budgetLimitTextTypeface =
                if (budgetLimitTextFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, budgetLimitTextFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (budgetLimitTextStyle == 0) {
            budgetLimitTextTypeface =
                    Typeface.create(budgetLimitTextTypeface, Typeface.BOLD)
        }

        budgetLimitTextPaint.run {
            style = Paint.Style.FILL
            textSize = budgetLimitTextSize
            textAlign = Paint.Align.LEFT
            color = budgetLimitTextColor
            typeface = budgetLimitTextTypeface
        }

        budgetLimitTextPadding =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_budgetLimitTextPadding,
                        budgetLimitTextPadding)

        budgetProjectionLineWidth =
        a.getDimension(
                R.styleable.BudgetLineView_blv_budgetProjectionLineWidth,
                budgetProjectionLineWidth)

        budgetProjectionLineColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_budgetProjectionLineColor,
                        budgetProjectionLineColor)

        budgetProjectionLinePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = budgetProjectionLineWidth
            strokeCap = Paint.Cap.ROUND
            color = budgetProjectionLineColor
        }

        dateProjectionLineWidth =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_dateProjectionLineWidth,
                        dateProjectionLineWidth)

        dateProjectionLineColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_dateProjectionLineColor,
                        dateProjectionLineColor)

        dateProjectionLinePaint.run {
            style = Paint.Style.STROKE
            strokeWidth = dateProjectionLineWidth
            strokeCap = Paint.Cap.ROUND
            color = dateProjectionLineColor
        }

        zeroOffset = a.getDimension(R.styleable.BudgetLineView_blv_zeroOffset, zeroOffset)

        dateTextSize =
                a.getDimension(R.styleable.BudgetLineView_blv_dateTextSize, dateTextSize)

        dateTextColor =
                a.getColor(R.styleable.BudgetLineView_blv_dateTextColor, dateTextColor)

        val dateTextStyle = a.getInt(R.styleable.BudgetLineView_blv_dateTextStyle, -1)

        val dateTextFontFamilyResId =
                a.getResourceId(R.styleable.BudgetLineView_blv_dateTextFontFamily, 0)

        var dateTextTypeface =
                if (dateTextFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, dateTextFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (dateTextStyle == 0) {
            dateTextTypeface =
                    Typeface.create(dateTextTypeface, Typeface.BOLD)
        }

        dateTextPaint.run {
            style = Paint.Style.FILL
            textSize = dateTextSize
            textAlign = Paint.Align.LEFT
            color = dateTextColor
            typeface = dateTextTypeface
        }

        dateRectPadding =
                a.getDimension(R.styleable.BudgetLineView_blv_dateRectPadding, dateRectPadding)

        dateRectColor =
                a.getColor(R.styleable.BudgetLineView_blv_dateRectColor, dateRectColor)

        dateRectPaint.run {
            style = Paint.Style.FILL
            color = dateRectColor
        }

        dateRectRadius =
                a.getDimension(R.styleable.BudgetLineView_blv_dateRectRadius, dateRectRadius)

        intersectionPointRadius =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_intersectionPointRadius,
                        intersectionPointRadius)

        intersectionPointColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_intersectionPointColor,
                        intersectionPointColor)

        intersectionPointPaint.run {
            style = Paint.Style.FILL
            color = intersectionPointColor
        }

        intersectionPointBorderWidth =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_intersectionPointBorderWidth,
                        intersectionPointBorderWidth)

        intersectionPointBorderColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_intersectionPointBorderColor,
                        intersectionPointBorderColor)

        intersectionPointBorderPaint.run {
            style = Paint.Style.STROKE
            strokeWidth = intersectionPointBorderWidth
            strokeCap = Paint.Cap.ROUND
            color = intersectionPointBorderColor
        }

        puckSize = a.getDimension(R.styleable.BudgetLineView_blv_puckSize, puckSize)
        puckGap = a.getDimension(R.styleable.BudgetLineView_blv_puckGap, puckGap)

        zeroOffset = a.getDimension(R.styleable.BudgetLineView_blv_zeroOffset, zeroOffset)

        intersectionPointTextBgColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_intersectionPointTextBgColor,
                        intersectionPointTextBgColor)

        intersectionPointTextBgPaint.run {
            style = Paint.Style.FILL
            color = intersectionPointTextBgColor
        }

        touchedPointAmountTextSize =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_touchedPointAmountTextSize,
                        touchedPointAmountTextSize)

        touchedPointAmountTextColor =
                a.getColor(
                        R.styleable.BudgetLineView_blv_touchedPointAmountTextColor,
                        touchedPointAmountTextColor)

        val touchedPointAmountTextStyle =
                a.getInt(R.styleable.BudgetLineView_blv_touchedPointAmountTextStyle, -1)

        val touchedPointAmountTextFontFamilyResId =
                a.getResourceId(
                        R.styleable.BudgetLineView_blv_touchedPointAmountTextFontFamily, 0)

        var touchedPointAmountTextTypeface =
                if (touchedPointAmountTextFontFamilyResId != 0) {
                    ResourcesCompat.getFont(context, touchedPointAmountTextFontFamilyResId)
                } else {
                    Typeface.DEFAULT
                }

        if (touchedPointAmountTextStyle == 0) {
            touchedPointAmountTextTypeface =
                    Typeface.create(touchedPointAmountTextTypeface, Typeface.BOLD)
        }

        touchedPointAmountTextPaint.run {
            style = Paint.Style.FILL
            textSize = touchedPointAmountTextSize
            color = touchedPointAmountTextColor
            typeface = touchedPointAmountTextTypeface
        }

        touchedPointAmountTextPadding =
                a.getDimension(
                        R.styleable.BudgetLineView_blv_touchedPointAmountTextPadding,
                        touchedPointAmountTextPadding)

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
                correctTouchedCoordinates()
            }
            toggle()
        }
    }

    private fun correctTouchedCoordinates() {
        intersectionPoints
                .minBy {
                    sqrt(((touchedX - it.x)*(touchedX - it.x)
                                    + (touchedY - it.y)*(touchedY - it.y)).toDouble())
                }
                ?.let {
                    touchedX = it.x
                    touchedY = it.y
                }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateContentRect()
        populatePath()
    }

    private fun populatePath() {

        if (isCurMonth) {
            todayLabel = "TODAY"
        } else {
            todayLabel = maxDate.toString()
            today = maxDate
        }

        updateGraphRect()
        populateBaseline()
        calculateYScaleFactor()
        populateBudgetLimit()
        populateSpentLine()

        // Note: Date Projection can only be displayed after Budget Projection
        //       as populateBudgetProjection() sets last (x,y) drawn
        populateBudgetProjection()
        populateDateProjection()

        populateDateRect()
        invalidate()
    }

    private fun populateDateRect() {

        val todayWidth = dateTextPaint.measureText(todayLabel)
        val budgetLimitReachDateWidth = dateTextPaint.measureText(budgetLimitReachDate.toString())
        val dateRectTop = graphRect.bottom + puckGap + puckSize
        val dateRectBottom = graphRect.bottom + puckGap + puckSize + dateRectPadding + dateTextSize + dateRectPadding

        dateRectPath.reset()

        when {
            today < budgetLimitReachDate && budgetLimitReachDate <= maxDate -> {

                todayRect.set(
                        today.getGraphX() - dateRectPadding - todayWidth - dateRectPadding,
                        dateRectTop,
                        today.getGraphX(),
                        dateRectBottom
                )

                budgetLimitReachDateRect.set(
                        budgetLimitReachDate.getGraphX(),
                        dateRectTop,
                        budgetLimitReachDate.getGraphX() + dateRectPadding + budgetLimitReachDateWidth + dateRectPadding,
                        dateRectBottom
                )

                dateRectPath.addRoundRect(
                        todayRect,
                        dateRectRadius,
                        dateRectRadius,
                        Path.Direction.CW
                )
                dateRectPath.addRoundRect(
                        budgetLimitReachDateRect,
                        dateRectRadius,
                        dateRectRadius,
                        Path.Direction.CCW
                )

                dateRectPath.moveTo(
                        todayRect.right - puckSize,
                        todayRect.top
                )
                dateRectPath.lineTo(
                        todayRect.right,
                        todayRect.top - puckSize
                )
                dateRectPath.lineTo(
                        todayRect.right,
                        todayRect.top + dateRectRadius
                )
                dateRectPath.close()

                dateRectPath.moveTo(
                        budgetLimitReachDateRect.left + puckSize,
                        budgetLimitReachDateRect.top
                )
                dateRectPath.lineTo(
                        budgetLimitReachDateRect.left,
                        budgetLimitReachDateRect.top - puckSize
                )
                dateRectPath.lineTo(
                        budgetLimitReachDateRect.left,
                        budgetLimitReachDateRect.top + dateRectRadius
                )
                dateRectPath.close()
            }
            today == budgetLimitReachDate || budgetLimitReachDate > maxDate -> {

                todayRect.set(
                        today.getGraphX() - (dateRectPadding + todayWidth + dateRectPadding) / 2,
                        dateRectTop,
                        today.getGraphX() + (dateRectPadding + todayWidth + dateRectPadding) / 2,
                        dateRectBottom
                )

                dateRectPath.addRoundRect(
                        todayRect,
                        dateRectRadius,
                        dateRectRadius,
                        Path.Direction.CW
                )

                dateRectPath.moveTo(
                        today.getGraphX() - puckSize,
                        todayRect.top
                )
                dateRectPath.lineTo(
                        today.getGraphX(),
                        todayRect.top - puckSize
                )
                dateRectPath.lineTo(
                        today.getGraphX() + puckSize,
                        todayRect.top
                )
                dateRectPath.close()
            }
            else -> {
                budgetLimitReachDateRect.set(
                        budgetLimitReachDate.getGraphX() - dateRectPadding - budgetLimitReachDateWidth - dateRectPadding,
                        dateRectTop,
                        budgetLimitReachDate.getGraphX(),
                        dateRectBottom
                )

                todayRect.set(
                        today.getGraphX(),
                        dateRectTop,
                        today.getGraphX() + dateRectPadding + todayWidth + dateRectPadding,
                        dateRectBottom
                )

                dateRectPath.addRoundRect(
                        budgetLimitReachDateRect,
                        dateRectRadius,
                        dateRectRadius,
                        Path.Direction.CW
                )
                dateRectPath.addRoundRect(
                        todayRect,
                        dateRectRadius,
                        dateRectRadius,
                        Path.Direction.CCW
                )

                dateRectPath.moveTo(
                        budgetLimitReachDateRect.right - puckSize,
                        budgetLimitReachDateRect.top
                )
                dateRectPath.lineTo(
                        budgetLimitReachDateRect.right,
                        budgetLimitReachDateRect.top - puckSize
                )
                dateRectPath.lineTo(
                        budgetLimitReachDateRect.right,
                        budgetLimitReachDateRect.top + dateRectRadius
                )
                dateRectPath.close()

                dateRectPath.moveTo(
                        todayRect.left + puckSize,
                        todayRect.top
                )
                dateRectPath.lineTo(
                        todayRect.left,
                        todayRect.top - puckSize
                )
                dateRectPath.lineTo(
                        todayRect.left,
                        todayRect.top + dateRectRadius
                )
                dateRectPath.close()
            }
        }
    }

    private fun populateDateProjection() {

        dateProjectionLinePath.reset()
        intersectionPointPath.reset()
        intersectionPoints =  mutableListOf()

        val dataMap = data.groupBy { it.x }.mapValues { it.value.sumByFloat { item -> item.y } }
        var curSum = 0f
        for (i in 1..today) {
            curSum += dataMap[i] ?: 0f
        }

        dateProjectionLinePath.moveTo(today.getGraphX(), curSum.getGraphY())
        dateProjectionLinePath.lineTo(today.getGraphX(), graphRect.bottom)

        intersectionPointPath.addCircle(
                today.getGraphX(), curSum.getGraphY(), intersectionPointRadius, Path.Direction.CW)

        intersectionPoints.add(PointF(today.getGraphX(), curSum.getGraphY()))

        if (today != budgetLimitReachDate) {
            dateProjectionLinePath.moveTo(budgetLimitReachDate.getGraphX(), budgetLimitShifted.getGraphY())
            dateProjectionLinePath.lineTo(budgetLimitReachDate.getGraphX(), graphRect.bottom)
            intersectionPointPath.addCircle(
                    budgetLimitReachDate.getGraphX(),
                    budgetLimitShifted.getGraphY(),
                    intersectionPointRadius,
                    Path.Direction.CW
            )
            intersectionPoints.add(
                    PointF(budgetLimitReachDate.getGraphX(), budgetLimitShifted.getGraphY()))
        }

        // draw point for the first date

        intersectionPointPath.addCircle(
                1.getGraphX(),
                (dataMap[1] ?: 0f).getGraphY(),
                intersectionPointRadius,
                Path.Direction.CW
        )

        intersectionPoints.add(PointF(1.getGraphX(), (dataMap[1] ?: 0f).getGraphY()))

        intersectionPointPath.addCircle(
                lastDrawnX,
                lastDrawnY,
                intersectionPointRadius,
                Path.Direction.CW
        )

        intersectionPoints.add(PointF(lastDrawnX, lastDrawnY))

    }

    private fun populateBudgetProjection() {

        val dataMap = data.groupBy { it.x }.mapValues { it.value.sumByFloat { item -> item.y } }
        var curSum = 0f
        for (i in 1..today) {
            curSum += dataMap[i] ?: 0f
        }

        budgetProjectionLinePath.reset()

        if (curSum < budgetLimitShifted) {
            budgetProjectionLinePath.moveTo(today.getGraphX(), curSum.getGraphY())
            if (budgetLimitReachDate > maxDate) {
                // if budgetLimitReachDate == Int.MAX_VALUE
                // i.e. slope of the line is 0
                if (budgetLimitReachDate == Int.MAX_VALUE) {

                    budgetProjectionLinePath.lineTo(
                            maxDate.getGraphX(), curSum.getGraphY())

                    lastDrawnX = maxDate.getGraphX()
                    lastDrawnY = curSum.getGraphY()

                } else {

                    val x1 = today.getGraphX()
                    val y1 = curSum.getGraphY()
                    val x2 = budgetLimitReachDate.getGraphX()
                    val y2 = budgetLimitShifted.getGraphY()
                    val x = maxDate.getGraphX()
                    val y = y1 - ((x1 - x) * (y1 - y2) / (x1 - x2))

                    budgetProjectionLinePath.lineTo(maxDate.getGraphX(), y)

                    lastDrawnX = maxDate.getGraphX()
                    lastDrawnY = y
                }

            } else {
                budgetProjectionLinePath.lineTo(
                        budgetLimitReachDate.getGraphX(),
                        budgetLimitShifted.getGraphY()
                )
                lastDrawnX = budgetLimitReachDate.getGraphX()
                lastDrawnY = budgetLimitShifted.getGraphY()
            }
        } else {
            lastDrawnX = today.getGraphX()
            lastDrawnY = curSum.getGraphY()
        }
    }

    // y = yScaleFactor * amount
    var yScaleFactor: Float = 0f

    private fun calculateYScaleFactor() {

        // proposed yScaleFactor using:
        // dist(budgetLimit) + budgetLimitTextSize + budgetLimitTextPadding == graphRect.height()
        // assuming all the points to be drawn fall inside the graphRect using above condition

        yScaleFactor =
                (graphRect.height() - zeroOffset - (budgetLimitTextSize + budgetLimitTextPadding)) / budgetLimitShifted

        val dataMap = data.groupBy { it.x }.mapValues { it.value.sumByFloat { item -> item.y } }
        var curSum = 0f
        for (i in 1..today) {
            curSum += dataMap[i] ?: 0f
        }

        // if spending > budget limit
        if (curSum > budgetLimitShifted) {
            // if according to proposed yScaleFactor
            // curSum is projected outside graphRect
            // change yScaleFactor to make curSum fall inside graphRect
            if (curSum * yScaleFactor + zeroOffset > graphRect.height()) {
                yScaleFactor = (graphRect.height() - zeroOffset) / curSum
            }
        }
    }

    private fun populateSpentLine() {

        val dataMap = data.groupBy { it.x }.mapValues { it.value.sumByFloat { item -> item.y } }
        var curSum = 0f
        spentLinePath.reset()
        for (day in 1..today) {
            curSum += dataMap[day] ?: 0f
            if (day == 1) {
                spentLinePath.moveTo(day.getGraphX(), curSum.getGraphY())
            }
            spentLinePath.lineTo(day.getGraphX(), curSum.getGraphY())
        }
    }

    private fun populateBudgetLimit() {
        budgetLimitLinePath.reset()
        budgetLimitLinePath.moveTo(graphRect.left, budgetLimitShifted.getGraphY())
        budgetLimitLinePath.lineTo(graphRect.right, budgetLimitShifted.getGraphY())
        budgetLimitLinePath.close()

        budgetLimitShiftPath.reset()
        budgetLimitShiftPath.addRect(
                graphRect.left,
                budgetLimitShifted.getGraphY(),
                graphRect.right,
                budgetLimit.getGraphY(),
                Path.Direction.CW
        )
    }

    private fun populateBaseline() {
        baselinePath.reset()
        baselinePath.moveTo(graphRect.left, graphRect.bottom)
        baselinePath.lineTo(graphRect.right, graphRect.bottom)
        baselinePath.close()

    }

    private fun updateGraphRect() {

        calculateBudgetLimitIntersections()

        // set graphRect to contentRect for date label measurements
        graphRect.set(contentRect)

        if (budgetLimitReachDate <= maxDate) {

            val leftMostDateLabelLeft =
            when {
                budgetLimitReachDate < today -> budgetLimitReachDate.getGraphX() - (dateTextPaint.measureText(budgetLimitReachDate.toString()) + 2 * dateRectPadding)
                budgetLimitReachDate == today -> today.getGraphX() - (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding) / 2
                else -> today.getGraphX() - (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding)
            }

            val rightMostDateLabelRight =
                    when {
                        budgetLimitReachDate < today -> today.getGraphX() + (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding)
                        budgetLimitReachDate == today -> today.getGraphX() + (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding) / 2
                        else -> budgetLimitReachDate.getGraphX() + (dateTextPaint.measureText(budgetLimitReachDate.toString()) + 2 * dateRectPadding)
                    }

            graphRect.left =
                    contentRect.left + Math.max(0f, contentRect.left - leftMostDateLabelLeft)

            graphRect.right =
                    contentRect.right - Math.max(0f, rightMostDateLabelRight - contentRect.right)

            graphRect.bottom =
                    contentRect.bottom - dateTextSize - 2 * dateRectPadding - puckSize - puckGap

            graphRect.top = contentRect.top

        } else {

            // if budgetLimit will never be reached in this month
            // i.e. projected budgetLimitReachDate > maxDate

            val leftMostDateLabelLeft =
                    today.getGraphX() - (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding) / 2

            val rightMostDateLabelRight =
                    today.getGraphX() + (dateTextPaint.measureText(todayLabel) + 2 * dateRectPadding) / 2

            graphRect.left =
                    contentRect.left + Math.max(0f, contentRect.left - leftMostDateLabelLeft)

            graphRect.right =
                    contentRect.right - Math.max(0f, rightMostDateLabelRight - contentRect.right)

            graphRect.bottom =
                    contentRect.bottom - dateTextSize - 2 * dateRectPadding - puckSize - puckGap

            graphRect.top = contentRect.top

        }

    }

    private fun calculateBudgetLimitIntersections() {

        val dataMap = data.groupBy { it.x }.mapValues { it.value.sumByFloat { item -> item.y } }
        var curSum = 0f

        budgetLimitReachDate = 0
        var exactBudgetLimitReachDateFound = false

        for (i in 1..today) {
            curSum += dataMap[i] ?: 0f
            if (curSum > budgetLimit) {
                // if curSum > budgetLimit for i == 1
                // shift intersection to 1
                budgetLimitReachDate = Math.max(1, i)
                break
            }
            if (curSum == budgetLimit) {
                exactBudgetLimitReachDateFound = true
                budgetLimitReachDate = i
                break
            }
        }

        var budgetLimitReached = true

        // [1] & [2] use
        // (y-y1)/(x-x1) = (y-y2)/(x-x2)
        // when (x,y) (x1,y1) (x2,y2) are on the same line

        if (budgetLimitReachDate == 0) {

            budgetLimitReached = false

            // if data of only 1 day is available, assume budgetLimit will never be reached
            budgetLimitReachDate = if (today == 1) {
                Int.MAX_VALUE
            } else {
                // [1]
                Math.floor((((today - 1) * (budgetLimit - (dataMap[1] ?: 0f))) / (curSum - (dataMap[1] ?: 0f))).toDouble() + 1).toInt()
            }
        }

        // avoid the case where budgetLimitShifted becomes 0
        // avoid the case when today is 1 as budgetLimitShifted will become NAN
        if (curSum != 0f
                && today != 1
                && !exactBudgetLimitReachDateFound
                && budgetLimitReached) {
            // in case budgetLimitReachDate != floor(budgetLimitReachDateAct)
            // [2]
            budgetLimitShifted = curSum
        } else {
            budgetLimitShifted = budgetLimit
        }

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

    override fun isChecked(): Boolean {
        return false
    }

    override fun toggle() {

        val newProgress = if (checkedX) 0f else 1f

        checkedX = !checkedX

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
                        duration = if (checkedX) 350L else 200L
                        start()
                    }
        }
    }

    override fun setChecked(checked: Boolean) {
        checkedX = checked
        progress = if (checkedX) {
            1f
        } else {
            0f
        }
    }

    private fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
        var sum = 0f
        for (element in this) {
            sum += selector(element)
        }
        return sum
    }

    /**
     * Converts date to x coordinate (according to euclid coordinate system) on the graphRect
     */
    private fun Int.getGraphX():Float {
        return graphRect.left + (this * ((graphRect.width()) / (maxDate + 1)))
    }

    /**
     * Converts amount to y coordinate (according to euclid coordinate system) on the graphRect
     */
    private fun Float.getGraphY():Float {
        return graphRect.top + (graphRect.height() - (this * yScaleFactor + zeroOffset))
    }

    /**
     * Converts amount back from the y coordinate (according to euclid coordinate system)
     * on the graphRect
     */
    private fun Float.getAmount():Float {
        return (-(this - graphRect.top - graphRect.height()) - zeroOffset) / yScaleFactor
    }

    data class Item(val x: Int,
                    val y: Float)
}