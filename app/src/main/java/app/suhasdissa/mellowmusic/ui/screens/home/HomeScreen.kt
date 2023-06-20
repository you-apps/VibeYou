package app.suhasdissa.mellowmusic.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destinations
import app.suhasdissa.mellowmusic.FavouriteSongs
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.Search
import app.suhasdissa.mellowmusic.Songs
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.SongViewModel
import app.suhasdissa.mellowmusic.ui.components.IconCard
import app.suhasdissa.mellowmusic.ui.components.MainScaffold
import app.suhasdissa.mellowmusic.ui.components.SongCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Destinations) -> Unit,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        songViewModel.getRecentSongs()
    }
    MainScaffold(fab = {
        FloatingActionButton(onClick = { onNavigate(Search) }) {
            Icon(Icons.Default.Search, null)
        }
    }, topBar = {
        TopAppBar(title = {
            Text(
                stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary
            )
        })
    }) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        IconCard(
                            { onNavigate(FavouriteSongs) },
                            Icons.Default.Favorite,
                            R.string.favourite_songs
                        )
                    }
                    item {
                        IconCard(
                            { onNavigate(Songs) },
                            Icons.Default.MusicNote,
                            R.string.songs
                        )
                    }
                }
            }

            item {
                Text(
                    stringResource(id = R.string.recent_songs),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(items = songViewModel.recentSongs) { item ->
                SongCard(
                    thumbnail = item.thumbnailUrl,
                    title = item.title,
                    artist = item.artistsText,
                    duration = item.durationText,
                    onClickVideoCard = {
                        playerViewModel.playSong(item)
                    })
            }
        }
    }
}