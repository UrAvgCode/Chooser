package com.uravgcode.chooser.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import androidx.core.graphics.ColorUtils
import kotlin.math.sin

class OrderCircle(x: Float, y: Float, radius: Float) : Circle(x, y, radius) {

    private val textPaint = Paint()
    private val textColor: Int

    private var number: Int? = null

    init {
        val hsvColor = FloatArray(3)
        Color.colorToHSV(color, hsvColor)
        hsvColor[1] = 0.2f
        hsvColor[2] = 1f

        textColor = Color.HSVToColor(hsvColor)
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        paint.color = if (coreRadius <= defaultRadius) {
            color
        } else {
            ColorUtils.blendARGB(color, Color.WHITE, 0.5f)
        }

        super.draw(canvas)
        number?.let {
            textPaint.textSize = coreRadius + radiusVariance * sin(time * 0.006).toFloat()
            val y = y - (textPaint.descent() + textPaint.ascent()) / 2
            val shadowOffset = textPaint.textSize * 0.04f

            textPaint.color = Color.argb(80, 0, 0, 0)
            canvas.drawText(it.toString(), x + shadowOffset, y + shadowOffset, textPaint)

            textPaint.color = textColor
            canvas.drawText(it.toString(), x, y, textPaint)
        }
    }

    override fun setWinner() {
        winnerCircle = true
        coreRadius *= 1.2f
        number = ++counter
    }

    override fun removeFinger() {
        if (winnerCircle) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ hasFinger = false }, 1500)
        } else {
            hasFinger = false
        }
    }

    companion object {
        var counter = 0
    }

}