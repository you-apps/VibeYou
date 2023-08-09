package app.suhasdissa.mellowmusic.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlaylistViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.state.PlaylistInfoState
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheetSearchPage
import coil.compose.AsyncImage

@Composable
fun PlaylistScreen(
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    MiniPlayerScaffold {
        when (val state = playlistViewModel.playlistInfoState) {
            PlaylistInfoState.Error -> IllustratedMessageScreen(
                image = R.drawable.sad_mellow,
                message = R.string.something_went_wrong
            )

            PlaylistInfoState.Loading -> LoadingScreen()
            is PlaylistInfoState.Success -> {
                var showSongSettings by remember { mutableStateOf(false) }
                var selectedSong by remember { mutableStateOf<Song?>(null) }
                Column {
                    Row(
                        Modifier.fillMaxWidth().padding(8.dp).padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(120.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp)),
                            model = state.thumbnail,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                text = state.name,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    SongList(
                        state.songs,
                        onClickCard = { song ->
                            playerViewModel.playSong(song)
                            playerViewModel.saveSong(song)
                        },
                        onLongPress = { song ->
                            selectedSong = song
                            showSongSettings = true
                        }
                    )
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
