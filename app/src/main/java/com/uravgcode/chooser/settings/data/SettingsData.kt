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
 * @description Data model for application settings
 */

package com.uravgcode.chooser.settings.data

import com.uravgcode.chooser.chooser.domain.Mode
import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val showSettingsHint: Boolean = true,

    val mode: Mode = Mode.SINGLE,
    val count: Int = 1,

    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,

    val fullScreen: Boolean = true,
    val additionalButtonPadding: Float = 0.0f,
    val circleSizeFactor: Float = 1.0f,

    val circleLifetime: Long = 1000L,
    val groupCircleLifetime: Long = 1000L,
    val orderCircleLifetime: Long = 1500L,
)
