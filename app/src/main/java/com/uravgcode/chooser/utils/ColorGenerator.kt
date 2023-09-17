package com.uravgcode.chooser.utils

import android.graphics.Color
import kotlin.random.Random

object ColorGenerator {
    private val colorPalette = mutableListOf<Int>()

    fun newColorPalette(amount: Int) {
        colorPalette.clear()
        var h = Random.nextInt(360)
        for (i in 0..<amount) {
            h = (h + 360 / amount) % 360
            val s = 0.5f + Random.nextFloat() / 2f
            val v = 0.5f + Random.nextFloat() / 2f
            val color = Color.HSVToColor(floatArrayOf(h.toFloat(), s, v))
            colorPalette.add(color)
        }
    }

    fun nextColor(): Int {
        if (colorPalette.isEmpty()) newColorPalette(5)
        val color = colorPalette[Random.nextInt(colorPalette.size)]
        colorPalette.remove(color)
        return color
    }
}