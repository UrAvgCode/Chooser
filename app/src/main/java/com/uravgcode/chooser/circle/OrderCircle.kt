package com.uravgcode.chooser.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.sin

class OrderCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius) {

    private val textPaint = Paint()

    private var number: Int? = null

    init {
        textPaint.color = Color.argb(200, 255, 255, 255)
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (number != null) {
            textPaint.textSize = coreRadius + radiusVariance * sin(time * 0.006).toFloat()
            val y = y - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(number.toString(), x, y, textPaint)
        }
    }

    override fun setWinner() {
        winnerCircle = true
        coreRadius *= 1.1f
        number = ++counter
    }

    companion object {
        var counter = 0
    }

}