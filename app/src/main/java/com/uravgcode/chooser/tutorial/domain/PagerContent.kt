package com.uravgcode.chooser.tutorial.domain

import com.uravgcode.chooser.R

val pagerContent = listOf(
    PageData(
        previewId = R.drawable.chooser_preview_animated,
        title = "Welcome to Chooser",
        description = "Make quick, unbiased decisions with a touch. " +
            "Place multiple fingers on screen, wait a brief moment, and Chooser will do the rest.",
    ),
    PageData(
        previewId = R.drawable.button_preview_animated,
        title = "How to Use",
        description = """
          <p>
            <b>Mode Button</b><br>
            Switch between Single, Group, and Order modes.
            <b>Long press</b> to access additional settings.
          </p>
          <br>
          <p>
            <b>Number Button</b><br>
            Adjust how many fingers to select or groups to create.
          </p>
        """,
    ),
    PageData(
        iconId = R.drawable.single_icon,
        previewId = R.drawable.single_preview_animated,
        title = "Single Mode",
        description = "Selects a random finger from all touching the screen.",
    ),
    PageData(
        iconId = R.drawable.group_icon,
        previewId = R.drawable.group_preview_animated,
        title = "Group Mode",
        description = "Divides all fingers into balanced teams or groups.",
    ),
    PageData(
        iconId = R.drawable.order_icon,
        previewId = R.drawable.order_preview_animated,
        title = "Order Mode",
        description = "Creates a random sequence of all fingers on screen.",
    ),
)
