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
import com.uravgcode.chooser.settings.data.SettingsData
import com.uravgcode.chooser.tutorial.presentation.component.PageIndicator
import com.uravgcode.chooser.tutorial.presentation.page.ButtonPage
import com.uravgcode.chooser.tutorial.presentation.page.GroupModePage
import com.uravgcode.chooser.tutorial.presentation.page.OrderModePage
import com.uravgcode.chooser.tutorial.presentation.page.SingleModePage
import com.uravgcode.chooser.tutorial.presentation.page.WelcomePage
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TutorialScreen(
    onComplete: () -> Unit,
    dataStore: DataStore<SettingsData>
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 5 })

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
                    when (page) {
                        0 -> WelcomePage()
                        1 -> ButtonPage()
                        2 -> SingleModePage()
                        3 -> GroupModePage()
                        4 -> OrderModePage()
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
