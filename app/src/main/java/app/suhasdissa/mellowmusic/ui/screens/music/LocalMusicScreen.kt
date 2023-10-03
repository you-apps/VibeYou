package app.suhasdissa.mellowmusic.ui.screens.music

import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destination
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.viewmodel.LocalSearchViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.LocalSongViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.ui.components.AlbumList
import app.suhasdissa.mellowmusic.ui.components.ArtistList
import app.suhasdissa.mellowmusic.ui.screens.songs.SongListView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocalMusicScreen(
    onNavigate: (Destination) -> Unit,
    localSearchViewModel: LocalSearchViewModel,
    localSongViewModel: LocalSongViewModel = viewModel(factory = LocalSongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    val pagerState = rememberPagerState { 3 }
    val scope = rememberCoroutineScope()
    Column {
        TabRow(selectedTabIndex = pagerState.currentPage, Modifier.fillMaxWidth()) {
            val view = LocalView.current
            Tab(selected = (pagerState.currentPage == 0), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
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
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    pagerState.animateScrollToPage(
                        1
                    )
                }
            }) {
                Text(
                    stringResource(R.string.albums),
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Tab(selected = (pagerState.currentPage == 2), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    pagerState.animateScrollToPage(
                        2
                    )
                }
            }) {
                Text(
                    stringResource(R.string.artists),
                    Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            when (index) {
                0 -> {
                    val view = LocalView.current
                    Scaffold(floatingActionButton = {
                        FloatingActionButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            playerViewModel.shuffleSongs(localSongViewModel.songs)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = stringResource(R.string.shuffle)
                            )
                        }
                    }) { innerPadding ->
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            SongListView(songs = localSongViewModel.songs)
                        }
                    }
                }

                1 -> AlbumList(items = localSongViewModel.albums, onClickCard = {
                    localSearchViewModel.getAlbumInfo(it.id.toLong(), it.title)
                    onNavigate(Destination.LocalPlaylists)
                }, onLongPress = {})

                2 -> ArtistList(
                    items = localSongViewModel.artists,
                    onClickCard = {},
                    onLongPress = {}
                )
            }
        }
    }
}
