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

package com.uravgcode.chooser.chooser.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.uravgcode.chooser.chooser.domain.Chooser
import com.uravgcode.chooser.chooser.domain.model.Mode
import com.uravgcode.chooser.chooser.presentation.button.AnimatedButton
import com.uravgcode.chooser.settings.domain.SettingsManager
import kotlinx.coroutines.launch

@Composable
fun ChooserScreen(onNavigate: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    var chooserMode by remember { mutableStateOf(SettingsManager.mode) }
    var chooserCount by remember { mutableIntStateOf(SettingsManager.count) }

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
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top),
    ) { padding ->
        val buttonTopPadding = remember {
            val minTopPadding = 24.dp
            if (SettingsManager.edgeToEdgeEnabled) {
                minTopPadding
            } else {
                max(padding.calculateTopPadding(), minTopPadding)
            } + SettingsManager.additionalTopPadding.dp
        }

        AndroidView(
            factory = { context ->
                Chooser(
                    context = context,
                    setButtonVisibility = { isVisible = it }
                )
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.mode = chooserMode
                view.count = chooserCount
            },
        )

        AnimatedButton(
            alignment = Alignment.TopStart,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible) {
                    chooserMode = chooserMode.next()
                    chooserCount = chooserMode.initialCount()
                    SettingsManager.mode = chooserMode
                    SettingsManager.count = chooserCount
                }
            },
            onLongClick = onNavigate,
            visible = isVisible,
            content = {
                Icon(
                    painter = painterResource(id = chooserMode.drawable()),
                    contentDescription = "Mode",
                )
            },
        )

        AnimatedButton(
            alignment = Alignment.TopEnd,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible) {
                    chooserCount = chooserMode.nextCount(chooserCount)
                    SettingsManager.count = chooserCount
                }
            },
            visible = chooserMode != Mode.ORDER && isVisible,
            content = {
                Text(
                    text = chooserCount.toString(),
                    fontSize = 36.sp
                )
            },
        )
    }
}
