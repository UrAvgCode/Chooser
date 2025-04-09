package com.uravgcode.chooser.tutorial.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uravgcode.chooser.tutorial.domain.pagerContent
import com.uravgcode.chooser.tutorial.presentation.component.PageIndicator
import com.uravgcode.chooser.tutorial.presentation.component.TutorialPage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TutorialScreen(onComplete: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pagerContent.size })

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { onComplete() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val isPageFullyVisible by remember {
                    derivedStateOf {
                        pagerState.currentPage == page &&
                            pagerState.currentPageOffsetFraction.absoluteValue < 0.1f
                    }
                }

                TutorialPage(
                    iconId = pagerContent[page].iconId,
                    previewId = pagerContent[page].previewId,
                    title = pagerContent[page].title,
                    description = pagerContent[page].description,
                    isVisible = isPageFullyVisible,
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

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
                            onComplete()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
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
