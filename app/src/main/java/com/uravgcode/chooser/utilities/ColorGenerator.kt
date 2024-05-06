package com.uravgcode.chooser.utilities

import android.graphics.Color
import kotlin.random.Random

object ColorGenerator {
    private val colorPalette = mutableListOf<Int>()

    fun generateRandomColorPalette(amount: Int) {
        colorPalette.clear()
        val hueStep = 360f / amount
        val initialHue = Random.nextFloat() * hueStep

        for (i in 0 until amount) {
            val hue = initialHue + hueStep * i
            val saturation = 0.5f + Random.nextFloat() / 2f
            val value = 0.5f + Random.nextFloat() / 2f
            val color = Color.HSVToColor(floatArrayOf(hue, saturation, value))
            colorPalette.add(color)
        }

        colorPalette.shuffle()
    }

    fun nextColor(): Int {
        if (colorPalette.isEmpty()) generateRandomColorPalette(5)
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