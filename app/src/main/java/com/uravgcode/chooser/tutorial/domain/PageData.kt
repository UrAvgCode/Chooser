package com.uravgcode.chooser.tutorial.domain

import androidx.annotation.DrawableRes

data class PageData(
    @DrawableRes val iconId: Int? = null,
    @DrawableRes val previewId: Int,
    val title: String,
    val description: String,
)
