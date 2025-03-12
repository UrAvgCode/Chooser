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
 * @description DisplaySettingsSection provides a settings section for adjusting display settings.
 */

package com.uravgcode.chooser.settings.presentation.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.uravgcode.chooser.settings.domain.SettingsManager
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPaddingSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowPercentSlider
import com.uravgcode.chooser.settings.presentation.row.SettingsRowSwitch

@Composable
fun DisplaySettingsSection() {
    var isEdgeToEdgeEnabled by remember { mutableStateOf(SettingsManager.edgeToEdgeEnabled) }
    var additionalTopPadding by remember { mutableFloatStateOf(SettingsManager.additionalTopPadding) }
    var circleSizeFactor by remember { mutableFloatStateOf(SettingsManager.circleSizeFactor) }

    SettingsSeparator("Display Settings")

    SettingsRowSwitch(
        title = "Enable Edge-to-Edge",
        isChecked = isEdgeToEdgeEnabled,
        onCheckedChange = { isChecked ->
            isEdgeToEdgeEnabled = isChecked
            SettingsManager.edgeToEdgeEnabled = isChecked
        }
    )

    SettingsRowPaddingSlider(
        title = "Additional Top Padding",
        value = additionalTopPadding,
        onValueChange = { sliderValue ->
            additionalTopPadding = sliderValue
            SettingsManager.additionalTopPadding = sliderValue
        },
        valueRange = 0f..50f,
        steps = 9
    )

    SettingsRowPercentSlider(
        title = "Circle Size",
        value = circleSizeFactor,
        onValueChange = { sliderValue ->
            circleSizeFactor = sliderValue
            SettingsManager.circleSizeFactor = sliderValue
        },
        valueRange = 0.5f..1.5f,
        steps = 9
    )
}
