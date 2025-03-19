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
 * @description Sealed class representing the different screens available in the app.
 */

package com.uravgcode.chooser.navigation.domain

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Tutorial : Screen()

    @Serializable
    data object Chooser : Screen()

    @Serializable
    data object Settings : Screen()
}
