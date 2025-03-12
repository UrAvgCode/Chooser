/*
 * Copyright (C) 2025 UrAvgCode
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author UrAvgCode
 * @description ColorManager manages the color palette of the circles.
 */

package com.uravgcode.chooser.chooser.domain.manager

import android.graphics.Color
import kotlin.random.Random

class ColorManager {
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
        return colorPalette.removeAt(0)
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
