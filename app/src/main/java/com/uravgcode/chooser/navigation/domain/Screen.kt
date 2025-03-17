package com.uravgcode.chooser.navigation.domain

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Chooser : Screen()

    @Serializable
    data object Settings : Screen()
}
