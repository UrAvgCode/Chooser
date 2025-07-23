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
    val hasSeenTutorial: Boolean = false,

    val mode: Mode = Mode.SINGLE,
    val count: Int = 1,

    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,

    val fullScreen: Boolean = true,
    val additionalButtonPadding: Float = 0.0f,
    val circleSizeFactor: Float = 1.0f,

    val singleDelay: Long = 3000L,
    val groupDelay: Long = 3000L,
    val orderDelay: Long = 3000L,

    val clearOnTouch: Boolean = true,
    val circleLifetime: Long = 1500L,
    val groupCircleLifetime: Long = 1500L,
    val orderCircleLifetime: Long = 2000L,
)
