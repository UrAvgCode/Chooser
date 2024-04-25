package com.uravgcode.chooser.utils

import android.graphics.Color
import kotlin.random.Random

object ColorGenerator {
    private val colorPalette = mutableListOf<Int>()

    fun newColorPalette(amount: Int) {
        colorPalette.clear()
        var hue = Random.nextFloat() * 360
        val hueIncrement = 360f / amount

        repeat(amount) {
            hue = (hue + hueIncrement) % 360
            val s = 0.5f + Random.nextFloat() / 2f
            val v = 0.5f + Random.nextFloat() / 2f
            val color = Color.HSVToColor(floatArrayOf(hue, s, v))
            colorPalette.add(color)
        }
        colorPalette.shuffle()
    }

    fun nextColor(): Int {
        if (colorPalette.isEmpty()) newColorPalette(5)
        return colorPalette.removeFirst()
    }

    fun averageColor(colors: List<Int>): Int {
        if (colors.isEmpty()) return Color.BLACK

        val size = colors.size
        val r = colors.sumOf { Color.red(it) } / size
        val g = colors.sumOf { Color.green(it) } / size
        val b = colors.sumOf { Color.blue(it) } / size

        return Color.rgb(r, g, b)
    }
}