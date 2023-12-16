package app.suhasdissa.vibeyou.ui.screens.playlists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.Destination
import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.viewmodel.PlaylistViewModel
import app.suhasdissa.vibeyou.ui.components.AlbumList
import app.suhasdissa.vibeyou.utils.asAlbum

@Composable
fun PlaylistsScreen(
    onNavigate: (Destination) -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel(
        factory = PlaylistViewModel.Factory
    )
) {
    val albums by playlistViewModel.albums.collectAsState()
    var selectedAlbum: Album? by remember { mutableStateOf(null) }
    AlbumList(items = albums.map { it.asAlbum }, onClickCard = {
        playlistViewModel.getPlaylistInfo(it)
        onNavigate(Destination.SavedPlaylists)
    }, onLongPress = {
        selectedAlbum = it
    })
    selectedAlbum?.let {
        PlaylistOptionsSheet(
            onDismissRequest = { selectedAlbum = null },
            album = it,
            playlistViewModel = playlistViewModel
        )
    }
}
