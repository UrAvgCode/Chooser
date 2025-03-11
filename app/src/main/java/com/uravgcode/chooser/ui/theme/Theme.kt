package com.uravgcode.chooser.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val darkColorScheme = darkColorScheme(
    primary = LightGray,
    inversePrimary = DarkGray,
    primaryContainer = LightGray,
    onPrimaryContainer = White,
    onPrimary = DarkGray,

    secondary = DarkGray,
    secondaryContainer = DarkGray,
    onSecondaryContainer = White,

    background = Black,
    onBackground = White,

    surface = Black,
    onSurface = White,
)

@Composable
fun ChooserTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme,
        typography = Typography,
        content = content
    )
}
