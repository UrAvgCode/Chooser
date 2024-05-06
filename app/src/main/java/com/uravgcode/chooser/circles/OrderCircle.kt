package com.uravgcode.chooser.circles

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import androidx.core.graphics.ColorUtils
import kotlin.math.sin

class OrderCircle(x: Float, y: Float, radius: Float, color: Int) : Circle(x, y, radius, color) {

    private val textPaint = Paint().apply {
        val hsvColor = FloatArray(3)
        Color.colorToHSV(this@OrderCircle.color, hsvColor)
        hsvColor[1] = 0.2f
        hsvColor[2] = 1f

        this.color = Color.HSVToColor(hsvColor)
        textAlign = Paint.Align.CENTER
    }

    private val textShadowPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        this.color = Color.argb(80, 0, 0, 0)
    }

    private var number: Int? = null

    override fun update(deltaTime: Int) {
        super.update(deltaTime)
        corePaint.color = if (coreRadius <= defaultRadius) {
            color
        } else {
            ColorUtils.blendARGB(color, Color.WHITE, 0.5f)
        }

        val textSize = coreRadius + radiusVariance * sin(timeMillis * 0.006).toFloat()
        textPaint.textSize = textSize
        textShadowPaint.textSize = textSize
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        number?.let {
            val y = y - (textPaint.descent() + textPaint.ascent()) / 2
            val shadowOffset = textPaint.textSize * 0.04f
            canvas.drawText(it.toString(), x + shadowOffset, y + shadowOffset, textShadowPaint)
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