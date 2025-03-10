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
 * @description ChooserScreen is the screen that displays the chooser view.
 */

package com.uravgcode.chooser.composables.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.uravgcode.chooser.composables.buttons.AnimatedButton
import com.uravgcode.chooser.utilities.Mode
import com.uravgcode.chooser.utilities.SettingsManager
import com.uravgcode.chooser.views.Chooser

@Composable
fun ChooserScreen(onNavigate: () -> Unit) {
    val isVisible = remember { mutableStateOf(true) }
    val chooserMode = remember { mutableStateOf(SettingsManager.mode) }
    val chooserCount = remember { mutableIntStateOf(SettingsManager.count) }

    AndroidView(
        factory = { context ->
            Chooser(
                context,
                setButtonVisibility = {
                    isVisible.value = it
                })
        },
        update = { view ->
            view.mode = chooserMode.value
            view.count = chooserCount.intValue
        },
        modifier = Modifier.fillMaxSize()
    )

    AnimatedButton(
        visible = isVisible.value,
        onClick = {
            if (isVisible.value) {
                chooserMode.value = chooserMode.value.next()
                chooserCount.intValue = chooserMode.value.initialCount()
                SettingsManager.mode = chooserMode.value
                SettingsManager.count = chooserCount.intValue
            }
        },
        onLongClick = onNavigate,
        content = {
            Icon(
                painter = painterResource(id = chooserMode.value.drawable()),
                contentDescription = "Mode"
            )
        },
        alignment = Alignment.TopStart,
        additionalTopPadding = SettingsManager.additionalTopPadding
    )

    AnimatedButton(
        visible = chooserMode.value != Mode.ORDER && isVisible.value,
        onClick = {
            if (isVisible.value) {
                chooserCount.intValue = chooserMode.value.nextCount(chooserCount.intValue)
                SettingsManager.count = chooserCount.intValue
            }
        },
        content = {
            Text(
                text = chooserCount.intValue.toString(),
                fontSize = 36.sp
            )
        },
        alignment = Alignment.TopEnd,
        additionalTopPadding = SettingsManager.additionalTopPadding
    )
}
