package app.suhasdissa.mellowmusic.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.ui.screens.songs.SongsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenView() {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pagerState.currentPage, Modifier.fillMaxWidth()) {
        Tab(selected = (pagerState.currentPage == 0), onClick = {
            scope.launch {
                pagerState.animateScrollToPage(
                    0
                )
            }
        }) {
            Text(
                stringResource(R.string.songs),
                Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Tab(selected = (pagerState.currentPage == 1), onClick = {
            scope.launch {
                pagerState.animateScrollToPage(
                    1
                )
            }
        }) {
            Text(
                stringResource(R.string.favourite_songs),
                Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    HorizontalPager(
        state = pagerState, modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> SongsScreen(showFavourites = false)
            1 -> SongsScreen(showFavourites = true)
        }
    }
}