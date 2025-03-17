package com.uravgcode.chooser.settings.data

import com.uravgcode.chooser.chooser.domain.model.Mode
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val mode: Mode = Mode.SINGLE,
    val count: Int = 1,

    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,

    val fullScreen: Boolean = true,
    val additionalButtonPadding: Float = 0.0f,
    val circleSizeFactor: Float = 1.0f,

    val circleLifetime: Long = 1000L,
    val groupCircleLifetime: Long = 1000L,
    val orderCircleLifetime: Long = 1500L,

    val showSettingsHint: Boolean = true,
)
