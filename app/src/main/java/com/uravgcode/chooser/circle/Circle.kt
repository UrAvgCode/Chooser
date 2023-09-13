package com.uravgcode.chooser.circle

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

open class Circle(var x: Float, var y: Float, radius: Float, var color: Int) {

    val center = RectF()
    val ring = RectF()

    val paint = Paint()
    val strokePaint = Paint()
    val strokePaintLight = Paint()

    var startAngle = Random.nextInt(360).toFloat()
    var sweepAngle = Random.nextInt(-360, 0).toFloat()

    var coreRadius = 0f
    private val defaultRadius = radius
    private val radiusVariance = radius * 0.08f

    var winnerCircle = false
    var hasFinger = true

    private var frames = 0

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

    fun updateValues(fps: Int) {
        val radius = coreRadius + radiusVariance * sin(frames / (0.17 * fps)).toFloat()
        val innerRadius = radius * 0.6f
        val strokeWidth = radius * 0.19f

        center.set(x - innerRadius, y - innerRadius, x + innerRadius, y + innerRadius)
        ring.set(x - radius, y - radius, x + radius, y + radius)

        paint.strokeWidth = strokeWidth
        strokePaint.strokeWidth = strokeWidth
        strokePaintLight.strokeWidth = strokeWidth

        startAngle = (startAngle + 300 / fps) % 360
        if (sweepAngle <= 360) sweepAngle += 450 / fps

        coreRadius = when (hasFinger) {
            true -> min(coreRadius + 600f / fps, defaultRadius)
            false -> max(coreRadius - 600f / fps, 0f)
        }

        frames++
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