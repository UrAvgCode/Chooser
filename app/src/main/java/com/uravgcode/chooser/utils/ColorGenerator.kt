package com.uravgcode.chooser.utils

import android.graphics.Color
import kotlin.random.Random

class ColorGenerator {
    private val colorPalette = mutableListOf<Int>()

    fun newColorPalette(amount: Int) {
        colorPalette.clear()
        var h = Random.nextInt(360)
        repeat(amount) {
            h = (h + 360 / amount) % 360
            val s = 0.5f + Random.nextFloat() / 2f
            val v = 0.5f + Random.nextFloat() / 2f
            val color = Color.HSVToColor(floatArrayOf(h.toFloat(), s, v))
            colorPalette.add(color)
        }
    }

    fun nextColor(): Int {
        if (colorPalette.isEmpty()) newColorPalette(5)
        val index = Random.nextInt(colorPalette.size)
        return colorPalette.removeAt(index)
    }

    fun averageColor(colors: List<Int>): Int {
        val size = colors.size

        val r = colors.sumOf { Color.red(it) } / size
        val g = colors.sumOf { Color.green(it) } / size
        val b = colors.sumOf { Color.blue(it) } / size

        return Color.rgb(r, g, b)
    }
}