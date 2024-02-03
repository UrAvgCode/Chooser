package com.uravgcode.chooser.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import com.uravgcode.chooser.utils.ColorGenerator
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

open class Circle(var x: Float, var y: Float, radius: Float, var color: Int = ColorGenerator.nextColor()) {

    private val center = RectF()
    private val ring = RectF()

    protected val paint = Paint()
    protected val strokePaint = Paint()
    protected val strokePaintLight = Paint()

    private var startAngle = Random.nextInt(360).toFloat()
    private var sweepAngle = Random.nextInt(-360, 0).toFloat()

    var coreRadius = 0f
    protected val defaultRadius = radius
    protected val radiusVariance = radius * 0.08f

    protected var winnerCircle = false
    protected var hasFinger = true

    protected var time = 0

    init {
        paint.color = color
        paint.style = Paint.Style.FILL_AND_STROKE

        strokePaint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeCap = Paint.Cap.ROUND

        strokePaintLight.color = Color.argb(65, 255, 255, 255)
        strokePaintLight.style = Paint.Style.STROKE
        strokePaintLight.strokeCap = Paint.Cap.ROUND
    }

    open fun update(deltaTime: Int) {
        val radius = coreRadius + radiusVariance * sin(time * 0.006).toFloat()
        val innerRadius = radius * 0.6f
        val strokeWidth = radius * 0.19f

        center.set(x - innerRadius, y - innerRadius, x + innerRadius, y + innerRadius)
        ring.set(x - radius, y - radius, x + radius, y + radius)

        paint.strokeWidth = strokeWidth
        strokePaint.strokeWidth = strokeWidth
        strokePaintLight.strokeWidth = strokeWidth

        startAngle = (startAngle + deltaTime * 0.3f) % 360
        if (sweepAngle <= 360) sweepAngle += deltaTime * 0.45f

        val target = if (hasFinger) defaultRadius else 0f
        coreRadius = approach(coreRadius, target, deltaTime * 0.6f)

        time += deltaTime
    }

    private fun approach(value: Float, target: Float, speed: Float): Float {
        return if (value < target) {
            min(value + speed, target)
        } else {
            max(value - speed, target)
        }
    }

    open fun draw(canvas: Canvas) {
        canvas.drawOval(center, paint)
        canvas.drawArc(ring, startAngle, sweepAngle, false, strokePaint)
        canvas.drawArc(center, startAngle + 180f, sweepAngle / 2f, false, strokePaintLight)
        canvas.drawArc(ring, startAngle, sweepAngle / 2f, false, strokePaintLight)
    }

    open fun setWinner() {
        winnerCircle = true
    }

    open fun isWinner(): Boolean {
        return winnerCircle
    }

    open fun removeFinger() {
        if (winnerCircle) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ hasFinger = false }, 1000)
        } else {
            hasFinger = false
        }
    }
}