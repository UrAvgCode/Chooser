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
 * @description SettingsRowSwitch is a component that provides a settings slider for time values.
 */

package com.uravgcode.chooser.settings.presentation.row

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRowTimeSlider(
    title: String,
    value: Long,
    onValueChange: (Long) -> Unit,
    valueRange: LongRange,
    steps: Int,
) {
    val floatValue = value.toFloat()
    val floatRange = valueRange.first.toFloat()..valueRange.last.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$title: ${"%.1f".format(value * 0.001)}s",
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Slider(
            value = floatValue,
            onValueChange = { newValue -> onValueChange(newValue.toLong()) },
            valueRange = floatRange,
            steps = steps,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
