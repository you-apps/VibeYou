package app.suhasdissa.vibeyou.presentation.screens.localmusic

import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.components.SongListView
import app.suhasdissa.vibeyou.presentation.screens.album.components.AlbumList
import app.suhasdissa.vibeyou.presentation.screens.artist.components.ArtistList
import app.suhasdissa.vibeyou.presentation.screens.localmusic.components.SortOrderDialog
import app.suhasdissa.vibeyou.presentation.screens.localmusic.model.LocalSongViewModel
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.Pref
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocalMusicScreen(
    onNavigate: (Destination) -> Unit,
    localSongViewModel: LocalSongViewModel = viewModel(factory = LocalSongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    val pagerState = rememberPagerState { 3 }
    val scope = rememberCoroutineScope()

    var showSortDialog by remember {
        mutableStateOf(false)
    }

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
                        Row {
                            FloatingActionButton(onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                playerViewModel.playSongs(localSongViewModel.songs)
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = stringResource(R.string.play_all)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            FloatingActionButton(onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                playerViewModel.playSongs(localSongViewModel.songs, shuffle = true)
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Shuffle,
                                    contentDescription = stringResource(R.string.shuffle)
                                )
                            }
                        }
                    }) { innerPadding ->
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .clickable {
                                        showSortDialog = true
                                    }
                            ) {
                                Text(text = stringResource(R.string.sort_order))
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Sort,
                                    contentDescription = null
                                )
                            }
                            SongListView(
                                songs = localSongViewModel.songs,
                                sortOrder = localSongViewModel.songsSortOrder
                            )
                        }
                    }
                }

                1 -> AlbumList(items = localSongViewModel.albums, onClickCard = {
                    onNavigate(Destination.LocalPlaylists(it))
                }, onLongPress = {})

                2 -> ArtistList(
                    items = localSongViewModel.artists,
                    onClickCard = {
                        onNavigate(Destination.LocalArtist(it))
                    },
                    onLongPress = {}
                )
            }
        }
    }

    if (showSortDialog) {
        SortOrderDialog(
            onDismissRequest = { showSortDialog = false },
            defaultSortOrder = localSongViewModel.songsSortOrder,
            defaultReverse = localSongViewModel.reverseSongs,
            onSortOrderChange = { sortOrder, reverse ->
                localSongViewModel.songsSortOrder = sortOrder
                localSongViewModel.reverseSongs = reverse
                localSongViewModel.updateSongsSortOrder()
                Pref.sharedPreferences.edit(true) {
                    putString(Pref.latestSongsSortOrderKey, sortOrder.toString())
                    putBoolean(Pref.latestReverseSongsPrefKey, reverse)
                }
            }
        )
    }
}
