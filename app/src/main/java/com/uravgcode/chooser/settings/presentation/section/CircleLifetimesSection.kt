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
 * @description CircleLifetimesSection provides a settings section for adjusting circles lifetimes.
 */

package com.uravgcode.chooser.settings.presentation.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import com.uravgcode.chooser.settings.domain.SettingsManager
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.row.SettingsRowTimeSlider

@Composable
fun CircleLifetimesSection() {
    val circleLifetime = remember { mutableLongStateOf(SettingsManager.circleLifetime) }
    val groupCircleLifetime = remember { mutableLongStateOf(SettingsManager.groupCircleLifetime) }
    val orderCircleLifetime = remember { mutableLongStateOf(SettingsManager.orderCircleLifetime) }

    SettingsSeparator("Circle Lifetimes")

    SettingsRowTimeSlider(
        title = "Circle Lifetime",
        value = circleLifetime.longValue,
        onValueChange = { sliderValue ->
            circleLifetime.longValue = sliderValue
            SettingsManager.circleLifetime = sliderValue
        },
        valueRange = 0L..3000L,
        steps = 5,
    )

    SettingsRowTimeSlider(
        title = "Group Circle Lifetime",
        value = groupCircleLifetime.longValue,
        onValueChange = { sliderValue ->
            groupCircleLifetime.longValue = sliderValue
            SettingsManager.groupCircleLifetime = sliderValue
        },
        valueRange = 0L..3000L,
        steps = 5,
    )

    SettingsRowTimeSlider(
        title = "Order Circle Lifetime",
        value = orderCircleLifetime.longValue,
        onValueChange = { sliderValue ->
            orderCircleLifetime.longValue = sliderValue
            SettingsManager.orderCircleLifetime = sliderValue
        },
        valueRange = 0L..3000L,
        steps = 5,
    )
}
