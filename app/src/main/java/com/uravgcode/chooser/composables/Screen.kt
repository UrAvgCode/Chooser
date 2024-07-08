package com.uravgcode.chooser.composables

sealed class Screen {
    data object Chooser : Screen()
    data object Settings : Screen()
}