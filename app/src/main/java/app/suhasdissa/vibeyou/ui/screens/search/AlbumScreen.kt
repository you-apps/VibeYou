package app.suhasdissa.vibeyou.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.viewmodel.PlayerViewModel
import app.suhasdissa.vibeyou.backend.viewmodel.PlaylistViewModel
import app.suhasdissa.vibeyou.backend.viewmodel.state.AlbumInfoState
import app.suhasdissa.vibeyou.ui.components.IllustratedMessageScreen
import app.suhasdissa.vibeyou.ui.components.LoadingScreen
import app.suhasdissa.vibeyou.ui.components.MiniPlayerScaffold
import app.suhasdissa.vibeyou.ui.components.SongCard
import app.suhasdissa.vibeyou.ui.components.SongSettingsSheetSearchPage
import coil.compose.AsyncImage

@Composable
fun AlbumScreen(
    state: AlbumInfoState,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    MiniPlayerScaffold {
        when (state) {
            AlbumInfoState.Error -> IllustratedMessageScreen(
                image = R.drawable.sad_mellow,
                message = R.string.something_went_wrong
            )

            AlbumInfoState.Loading -> LoadingScreen()
            is AlbumInfoState.Success -> {
                var showSongSettings by remember { mutableStateOf(false) }
                var selectedSong by remember { mutableStateOf<Song?>(null) }
                LazyColumn(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    item {
                        Column(
                            Modifier.fillMaxWidth().padding(8.dp).padding(bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp, vertical = 20.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp)),
                                model = state.album.thumbnailUri,
                                contentDescription = stringResource(id = R.string.album_art),
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.music_placeholder)
                            )
                            Column(Modifier.fillMaxWidth().padding(8.dp)) {
                                Text(
                                    text = state.album.title,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = state.album.artistsText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                state.album.numberOfSongs?.let {
                                    Text(
                                        text = "$it ${stringResource(id = R.string.songs)}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(modifier = Modifier.weight(1f), onClick = {
                                    playerViewModel.playSongs(state.songs, shuffle = false)
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = null
                                    )
                                    Text(text = stringResource(id = R.string.play_all))
                                }
                                Button(modifier = Modifier.weight(1f), onClick = {
                                    playerViewModel.playSongs(state.songs, shuffle = true)
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Shuffle,
                                        contentDescription = null
                                    )
                                    Text(text = stringResource(id = R.string.shuffle))
                                }
                            }
                            if (!state.album.isLocal) {
                                val context = LocalContext.current
                                FilledTonalButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        playlistViewModel.newPlaylistWithSongs(
                                            state.album,
                                            state.songs
                                        )
                                        Toast.makeText(
                                            context,
                                            context.getString(
                                                R.string.added_all_the_songs_to_the_library
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.LibraryAdd,
                                        contentDescription = null
                                    )
                                    Text(text = stringResource(R.string.add_to_library))
                                }
                            }
                            Divider()
                        }
                    }
                    items(items = state.songs) { item ->
                        SongCard(
                            song = item,
                            onClickCard = {
                                playerViewModel.playSong(item)
                                if (!item.isLocal) {
                                    playerViewModel.saveSong(item)
                                }
                            },
                            onLongPress = {
                                selectedSong = item
                                showSongSettings = true
                            }
                        )
                    }
                }

                if (showSongSettings) {
                    selectedSong?.let {
                        SongSettingsSheetSearchPage(
                            onDismissRequest = { showSongSettings = false },
                            song = it
                        )
                    }
                }
            }
        }
    }
}
