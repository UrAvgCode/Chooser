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
 * @description TimeSlider is a component that provides a settings slider for time values.
 */

package com.uravgcode.chooser.settings.presentation.slider

import androidx.compose.runtime.Composable
import kotlin.math.roundToLong

@Composable
fun TimeSlider(
    title: String,
    value: Long,
    onValueChange: (Long) -> Unit,
    valueRange: ClosedRange<Long> = 0L..3000L,
    steps: Int = 5
) {
    SettingsSlider(
        title = title,
        value = value.toFloat(),
        onValueChange = { onValueChange(it.roundToLong()) },
        valueRange = valueRange.start.toFloat()..valueRange.endInclusive.toFloat(),
        steps = steps,
        valueFormatter = { "${it / 1000}s" }
    )
}
