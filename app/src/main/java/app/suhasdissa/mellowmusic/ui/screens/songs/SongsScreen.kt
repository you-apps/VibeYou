package app.suhasdissa.mellowmusic.ui.screens.songs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.SongViewModel
import app.suhasdissa.mellowmusic.ui.components.MainScaffold
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    showFavourites: Boolean,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        if (showFavourites) {
            songViewModel.getFavouriteSongs()
        } else {
            songViewModel.getAllSongs()
        }
    }
    var showSongSettings by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    MainScaffold(fab = {
        FloatingActionButton(onClick = { if (showFavourites) playerViewModel.shuffleFavourites() else playerViewModel.shuffleAll() }) {
            Icon(imageVector = Icons.Default.Shuffle, contentDescription = "Shuffle")
        }
    },
        topBar = {
            TopAppBar(title = {
                Text(
                    if (showFavourites) stringResource(id = R.string.favourite_songs) else stringResource(
                        id = R.string.songs
                    )
                )
            })
        }) {
        SongList(
            items = (if (showFavourites) songViewModel.favSongs else songViewModel.songs),
            onClickCard = { song -> playerViewModel.schedulePlay(song) },
            onLongPress = { song ->
                selectedSong = song
                showSongSettings = true
            })
    }
    if (showSongSettings) {
        selectedSong?.let {
            SongSettingsSheet(
                onDismissRequest = { showSongSettings = false },
                song = selectedSong!!
            )
        }
    }
}