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
import com.uravgcode.chooser.tutorial.presentation.page.ButtonPage
import com.uravgcode.chooser.tutorial.presentation.page.ModePage
import com.uravgcode.chooser.tutorial.presentation.page.WelcomePage
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val isPageFullyVisible = pagerState.currentPage == page &&
                        pagerState.currentPageOffsetFraction.absoluteValue < 0.1f

                    when (Page.entries[page]) {
                        Page.WELCOME -> WelcomePage()
                        Page.BUTTON -> ButtonPage()
                        Page.SINGLE_MODE -> ModePage(
                            iconId = R.drawable.single_icon,
                            previewId = R.drawable.single_preview_animated,
                            title = "Single Mode",
                            description = "Chooses a random finger from all placed on screen",
                            previewDescription = "Perfect for making quick decisions or selecting a winner",
                            isVisible = isPageFullyVisible,
                        )

                        Page.GROUP_MODE -> ModePage(
                            iconId = R.drawable.group_icon,
                            previewId = R.drawable.group_preview_animated,
                            title = "Group Mode",
                            description = "Divides all fingers into equal teams",
                            previewDescription = "Great for forming teams or groups",
                            isVisible = isPageFullyVisible,
                        )

                        Page.ORDER_MODE -> ModePage(
                            iconId = R.drawable.order_icon,
                            previewId = R.drawable.order_preview_animated,
                            title = "Order Mode",
                            description = "Assigns a random order to all fingers",
                            previewDescription = "Perfect for determining turn order in games",
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
