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

package com.uravgcode.chooser.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.uravgcode.chooser.ui.components.buttons.AnimatedButton
import com.uravgcode.chooser.utilities.Mode
import com.uravgcode.chooser.utilities.SettingsManager
import com.uravgcode.chooser.views.Chooser
import kotlinx.coroutines.launch

@Composable
fun ChooserScreen(onNavigate: () -> Unit) {
    val isVisible = remember { mutableStateOf(true) }
    val chooserMode = remember { mutableStateOf(SettingsManager.mode) }
    val chooserCount = remember { mutableIntStateOf(SettingsManager.count) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        if (SettingsManager.showSettingsHint) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Long Press the Mode Icon to Open Settings",
                    actionLabel = "OK",
                    duration = SnackbarDuration.Long
                )
            }
            SettingsManager.showSettingsHint = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
    ) { padding ->
        val buttonTopPadding = if (SettingsManager.edgeToEdgeEnabled) {
            24.dp
        } else {
            max(padding.calculateTopPadding(), 24.dp)
        } + SettingsManager.additionalTopPadding.dp

        AndroidView(
            factory = { context ->
                Chooser(
                    context,
                    setButtonVisibility = {
                        isVisible.value = it
                    })
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.mode = chooserMode.value
                view.count = chooserCount.intValue
            },
        )

        AnimatedButton(
            alignment = Alignment.TopStart,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible.value) {
                    chooserMode.value = chooserMode.value.next()
                    chooserCount.intValue = chooserMode.value.initialCount()
                    SettingsManager.mode = chooserMode.value
                    SettingsManager.count = chooserCount.intValue
                }
            },
            onLongClick = onNavigate,
            visible = isVisible.value,
            content = {
                Icon(
                    painter = painterResource(id = chooserMode.value.drawable()),
                    contentDescription = "Mode"
                )
            },
        )

        AnimatedButton(
            alignment = Alignment.TopEnd,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible.value) {
                    chooserCount.intValue = chooserMode.value.nextCount(chooserCount.intValue)
                    SettingsManager.count = chooserCount.intValue
                }
            },
            visible = chooserMode.value != Mode.ORDER && isVisible.value,
            content = {
                Text(
                    text = chooserCount.intValue.toString(),
                    fontSize = 36.sp
                )
            },
        )
    }
}
