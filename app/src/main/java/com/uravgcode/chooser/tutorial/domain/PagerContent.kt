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
 * @description A list of page data for the tutorial pager.
 */

package com.uravgcode.chooser.tutorial.domain

import com.uravgcode.chooser.R

val pagerContent = listOf(
    PageData(
        previewId = R.drawable.chooser_preview_animated,
        title = R.string.tutorial_welcome_title,
        description = R.string.tutorial_welcome_text,
    ),
    PageData(
        previewId = R.drawable.button_preview_animated,
        title = R.string.tutorial_how_to_use_title,
        description = R.string.tutorial_how_to_use_text,
    ),
    PageData(
        iconId = R.drawable.single_icon,
        previewId = R.drawable.single_preview_animated,
        title = R.string.tutorial_single_mode_title,
        description = R.string.tutorial_single_mode_text,
    ),
    PageData(
        iconId = R.drawable.group_icon,
        previewId = R.drawable.group_preview_animated,
        title = R.string.tutorial_group_mode_title,
        description = R.string.tutorial_group_mode_text,
    ),
    PageData(
        iconId = R.drawable.order_icon,
        previewId = R.drawable.order_preview_animated,
        title = R.string.tutorial_order_mode_title,
        description = R.string.tutorial_order_mode_text,
    ),
)
