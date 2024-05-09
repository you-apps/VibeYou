package app.suhasdissa.vibeyou.presentation.screens.onlinemusic

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.screens.onlinemusic.components.SongsScreen
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.playlists.PlaylistsScreen
import kotlinx.coroutines.launch

@Composable
fun OnlineMusicScreen(
    onNavigate: (Destination) -> Unit,
    playerViewModel: PlayerViewModel,
    onDrawerOpen: (() -> Unit)?,
) {
    val view = LocalView.current
    Scaffold(topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onDrawerOpen != null) {
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onDrawerOpen()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Open Navigation Drawer",
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Card(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onNavigate(Destination.OnlineSearch)
                    },
                shape = RoundedCornerShape(40),
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                    )
                    Text(stringResource(R.string.search_songs))
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                    )
                }
            }
        }
    }) { pV ->
        MusicScreenContent(
            modifier =
            Modifier
                .consumeWindowInsets(pV)
                .padding(pV),
            onNavigate,
            playerViewModel,
        )
    }
}

@Composable
private fun MusicScreenContent(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit,
    playerViewModel: PlayerViewModel,
) {
    val pagerState = rememberPagerState { 3 }
    val scope = rememberCoroutineScope()
    Column(modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage, Modifier.fillMaxWidth()) {
            val view = LocalView.current
            Tab(selected = (pagerState.currentPage == 0), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    pagerState.animateScrollToPage(
                        0,
                    )
                }
            }) {
                Text(
                    stringResource(R.string.songs),
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Tab(selected = (pagerState.currentPage == 1), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    pagerState.animateScrollToPage(
                        1,
                    )
                }
            }) {
                Text(
                    stringResource(R.string.favourite_songs),
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Tab(selected = (pagerState.currentPage == 2), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    pagerState.animateScrollToPage(
                        2,
                    )
                }
            }) {
                Text(
                    stringResource(R.string.playlists),
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            when (index) {
                0 -> SongsScreen(showFavourites = false, playerViewModel)
                1 -> SongsScreen(showFavourites = true, playerViewModel)
                2 -> PlaylistsScreen(onNavigate = onNavigate)
            }
        }
    }
}
