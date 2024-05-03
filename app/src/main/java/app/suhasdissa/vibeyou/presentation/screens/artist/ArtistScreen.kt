package app.suhasdissa.vibeyou.presentation.screens.artist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.presentation.components.IllustratedMessageScreen
import app.suhasdissa.vibeyou.presentation.components.LoadingScreen
import app.suhasdissa.vibeyou.presentation.screens.album.components.AlbumList
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.ArtistInfoState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    onClickAlbum: (Album) -> Unit,
    state: ArtistInfoState
) {
    Scaffold(topBar = {
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
    }) { pV ->
        Column(Modifier.padding(pV)) {
            when (state) {
                ArtistInfoState.Error -> IllustratedMessageScreen(
                    image = R.drawable.ic_launcher_monochrome,
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
}
