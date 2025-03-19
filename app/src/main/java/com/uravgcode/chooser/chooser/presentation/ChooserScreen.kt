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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.chooser.domain.Mode
import com.uravgcode.chooser.chooser.presentation.button.AnimatedButton
import com.uravgcode.chooser.chooser.presentation.circle.Circle
import com.uravgcode.chooser.chooser.presentation.circle.GroupCircle
import com.uravgcode.chooser.chooser.presentation.circle.OrderCircle
import com.uravgcode.chooser.chooser.presentation.manager.SoundManager
import com.uravgcode.chooser.settings.data.SettingsData
import kotlinx.coroutines.launch

@Composable
fun ChooserScreen(
    onNavigate: () -> Unit,
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    val settings by dataStore.data.collectAsState(initial = SettingsData())

    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(settings) {
        SoundManager.soundEnabled = settings.soundEnabled
        Chooser.vibrationEnabled = settings.vibrationEnabled
        Chooser.circleSizeFactor = settings.circleSizeFactor

        Circle.circleLifetime = settings.circleLifetime
        GroupCircle.circleLifetime = settings.groupCircleLifetime
        OrderCircle.circleLifetime = settings.orderCircleLifetime
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top),
    ) { padding ->
        val buttonTopPadding = remember(settings.fullScreen, settings.additionalButtonPadding) {
            val minTopPadding = 24.dp
            if (settings.fullScreen) {
                minTopPadding
            } else {
                max(padding.calculateTopPadding(), minTopPadding)
            } + settings.additionalButtonPadding.dp
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
                view.mode = settings.mode
                view.count = settings.count
            },
        )

        AnimatedButton(
            alignment = Alignment.TopStart,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible) {
                    coroutineScope.launch {
                        dataStore.updateData {
                            it.copy(
                                mode = settings.mode.next(),
                                count = settings.mode.next().initialCount()
                            )
                        }
                    }
                }
            },
            onLongClick = onNavigate,
            visible = isVisible,
            content = {
                Icon(
                    painter = painterResource(id = settings.mode.drawable()),
                    contentDescription = "Mode",
                    modifier = Modifier.size(36.dp)
                )
            },
        )

        AnimatedButton(
            alignment = Alignment.TopEnd,
            topPadding = buttonTopPadding,
            onClick = {
                if (isVisible) {
                    coroutineScope.launch {
                        dataStore.updateData {
                            it.copy(
                                count = settings.mode.nextCount(settings.count)
                            )
                        }
                    }
                }
            },
            visible = settings.mode != Mode.ORDER && isVisible,
            content = {
                Text(
                    text = settings.count.toString(),
                    fontSize = 36.sp
                )
            },
        )
    }
}
