package app.suhasdissa.mellowmusic.ui.screens.songs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.SongViewModel
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheet

@Composable
fun SongListView(
    showFavourites: Boolean,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    var showSongSettings by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val songs by if (showFavourites) songViewModel.favSongs.collectAsState() else songViewModel.songs.collectAsState()
    SongList(
        items = songs,
        onClickCard = { song -> playerViewModel.playSong(song) },
        onLongPress = { song ->
            selectedSong = song
            showSongSettings = true
        }
    )
    if (showSongSettings) {
        selectedSong?.let {
            SongSettingsSheet(
                onDismissRequest = { showSongSettings = false },
                song = selectedSong!!
            )
        }
    }
}
