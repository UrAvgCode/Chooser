package com.uravgcode.chooser.composables.screens

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
import com.uravgcode.chooser.composables.AnimatedButton
import com.uravgcode.chooser.utilities.Mode
import com.uravgcode.chooser.utilities.SettingsManager
import com.uravgcode.chooser.views.Chooser

@Composable
fun ChooserScreen(
    settings: SettingsManager,
    onNavigate: () -> Unit
) {
    val chooserMode = remember { mutableStateOf(settings.getMode()) }
    val chooserCount = remember { mutableIntStateOf(settings.getCount()) }
    val isVisible = remember { mutableStateOf(true) }

    AndroidView(
        factory = { context ->
            Chooser(context, setButtonVisibility = {
                isVisible.value = it
            })
        },
        update = { view ->
            view.mode = chooserMode.value
            view.count = chooserCount.intValue
        },
        modifier = Modifier
    )

    AnimatedButton(
        visible = isVisible.value,
        onClick = {
            if (isVisible.value) {
                chooserMode.value = chooserMode.value.next()
                chooserCount.intValue = chooserMode.value.initialCount()
                settings.setMode(chooserMode.value)
            }
        },
        content = {
            Icon(
                painter = painterResource(id = chooserMode.value.drawable()),
                contentDescription = "Mode"
            )
        },
        alignment = Alignment.TopStart
    )

    AnimatedButton(
        visible = chooserMode.value != Mode.ORDER && isVisible.value,
        onClick = {
            if (isVisible.value) {
                chooserCount.intValue = chooserMode.value.nextCount(chooserCount.intValue)
                settings.setCount(chooserCount.intValue)
            }
        },
        content = {
            Text(
                text = chooserCount.intValue.toString(),
                fontSize = 36.sp
            )
        },
        alignment = Alignment.TopEnd
    )
}