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
 * @description GeneralSettingsSection provides a settings section for adjusting general settings.
 */

package com.uravgcode.chooser.settings.presentation.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.uravgcode.chooser.settings.domain.SettingsManager
import com.uravgcode.chooser.settings.presentation.component.SettingsSeparator
import com.uravgcode.chooser.settings.presentation.row.SettingsRowSwitch

@Composable
fun GeneralSettingsSection() {
    var isSoundEnabled by remember { mutableStateOf(SettingsManager.soundEnabled) }
    var isVibrationEnabled by remember { mutableStateOf(SettingsManager.vibrationEnabled) }

    SettingsSeparator(
        heading = "General Settings",
        showDivider = false,
    )

    SettingsRowSwitch(
        title = "Enable Sound",
        isChecked = isSoundEnabled,
        onCheckedChange = { isChecked ->
            isSoundEnabled = isChecked
            SettingsManager.soundEnabled = isChecked
        }
    )

    SettingsRowSwitch(
        title = "Enable Vibration",
        isChecked = isVibrationEnabled,
        onCheckedChange = { isChecked ->
            isVibrationEnabled = isChecked
            SettingsManager.vibrationEnabled = isChecked
        }
    )
}
