package com.uravgcode.chooser.tutorial.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import com.uravgcode.chooser.R
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.tutorial.domain.Page
import com.uravgcode.chooser.tutorial.presentation.component.PageIndicator
import com.uravgcode.chooser.tutorial.presentation.component.TutorialPage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TutorialScreen(
    onComplete: () -> Unit,
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Page.entries.size })

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val isPageFullyVisible = pagerState.currentPage == page &&
                        pagerState.currentPageOffsetFraction.absoluteValue < 0.1f

                    when (Page.entries[page]) {
                        Page.WELCOME -> TutorialPage(
                            previewId = R.drawable.chooser_preview_animated,
                            title = "Welcome to Chooser",
                            description = "Make quick, unbiased decisions with a touch. Place multiple fingers on screen, wait a brief moment, and Chooser will do the rest.",
                            isVisible = isPageFullyVisible,
                        )

                        Page.BUTTON -> TutorialPage(
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
                            isVisible = true,
                        )

                        Page.SINGLE_MODE -> TutorialPage(
                            iconId = R.drawable.single_icon,
                            previewId = R.drawable.single_preview_animated,
                            title = "Single Mode",
                            description = "Selects a random finger from all touching the screen.",
                            isVisible = isPageFullyVisible,
                        )

                        Page.GROUP_MODE -> TutorialPage(
                            iconId = R.drawable.group_icon,
                            previewId = R.drawable.group_preview_animated,
                            title = "Group Mode",
                            description = "Divides all fingers into balanced teams or groups.",
                            isVisible = isPageFullyVisible,
                        )

                        Page.ORDER_MODE -> TutorialPage(
                            iconId = R.drawable.order_icon,
                            previewId = R.drawable.order_preview_animated,
                            title = "Order Mode",
                            description = "Creates a random sequence of all fingers on screen.",
                            isVisible = isPageFullyVisible,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                PageIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    pagerState = pagerState
                )

                IconButton(
                    onClick = {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            coroutineScope.launch {
                                dataStore.updateData {
                                    it.copy(hasSeenTutorial = true)
                                }
                                onComplete()
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (pagerState.currentPage < pagerState.pageCount - 1)
                            Icons.AutoMirrored.Filled.ArrowForward
                        else
                            Icons.Default.Check,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}
