package app.suhasdissa.mellowmusic.ui.screens.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destinations
import app.suhasdissa.mellowmusic.Playlists
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.viewmodel.ArtistViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlaylistViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.state.ArtistInfoState
import app.suhasdissa.mellowmusic.ui.components.AlbumList
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    onNavigate: (Destinations) -> Unit,
    artistViewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    MiniPlayerScaffold(topBar = {
        TopAppBar(title = {
            when (val state = artistViewModel.artistInfoState) {
                is ArtistInfoState.Success -> Text(text = state.name)
                else -> Text(stringResource(R.string.artist))
            }
        }, scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior())
    }) {
        when (val state = artistViewModel.artistInfoState) {
            ArtistInfoState.Error -> IllustratedMessageScreen(
                image = R.drawable.sad_mellow,
                message = R.string.something_went_wrong
            )

            ArtistInfoState.Loading -> LoadingScreen()
            is ArtistInfoState.Success -> {
                AlbumList(items = state.playlists, onClickCard = {
                    playlistViewModel.getPlaylistInfo(it.playlistId)
                    onNavigate(Playlists)
                }, onLongPress = {
                })
            }
        }
    }
}
