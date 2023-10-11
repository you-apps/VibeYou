package app.suhasdissa.mellowmusic.ui.screens.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.viewmodel.state.ArtistInfoState
import app.suhasdissa.mellowmusic.ui.components.AlbumList
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    onClickAlbum: (Album) -> Unit,
    state: ArtistInfoState
) {
    MiniPlayerScaffold(topBar = {
        TopAppBar(title = {
            when (state) {
                is ArtistInfoState.Success -> Text(
                    text = state.artist.artistsText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                else -> Text(stringResource(R.string.artist))
            }
        }, scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior())
    }) {
        when (state) {
            ArtistInfoState.Error -> IllustratedMessageScreen(
                image = R.drawable.sad_mellow,
                message = R.string.something_went_wrong
            )

            ArtistInfoState.Loading -> LoadingScreen()
            is ArtistInfoState.Success -> {
                AlbumList(items = state.playlists, onClickCard = {
                    onClickAlbum.invoke(it)
                }, onLongPress = {
                })
            }
        }
    }
}
