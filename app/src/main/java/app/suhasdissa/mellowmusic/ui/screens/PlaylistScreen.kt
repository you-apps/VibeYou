package app.suhasdissa.mellowmusic.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.request.ImageRequest

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
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        AsyncImage(
                            modifier = Modifier
                                .size(180.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp)),
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(state.thumbnail).crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Text(text = state.name)
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
