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
 * @description PercentSlider is a component that provides a settings slider for percent values.
 */

package com.uravgcode.chooser.settings.presentation.slider

import androidx.compose.runtime.Composable
import kotlin.math.roundToInt

@Composable
fun PercentSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    SettingsSlider(
        title = title,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        valueFormatter = { "${(it * 100).roundToInt()}%" }
    )
}
