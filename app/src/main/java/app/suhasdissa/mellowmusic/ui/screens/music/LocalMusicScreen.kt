package app.suhasdissa.mellowmusic.ui.screens.music

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.LocalSongViewModel
import app.suhasdissa.mellowmusic.ui.screens.songs.SongListView

@Composable
fun LocalMusicScreen(
    localSongViewModel: LocalSongViewModel = viewModel(factory = LocalSongViewModel.Factory)
) {
    SongListView(songs = localSongViewModel.songs)
}
