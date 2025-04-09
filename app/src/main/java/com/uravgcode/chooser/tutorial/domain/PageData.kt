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
 * @description Data class that defines the content structure for a single tutorial page.
 */

package com.uravgcode.chooser.tutorial.domain

import androidx.annotation.DrawableRes

data class PageData(
    @DrawableRes val iconId: Int? = null,
    @DrawableRes val previewId: Int,
    val title: String,
    val description: String,
)
