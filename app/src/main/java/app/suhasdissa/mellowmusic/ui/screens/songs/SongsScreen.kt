package app.suhasdissa.mellowmusic.ui.screens.songs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.ui.components.MainScaffold

@Composable
fun SongsScreen(
    showFavourites: Boolean,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    MainScaffold(fab = {
        FloatingActionButton(onClick = { if (showFavourites) playerViewModel.shuffleFavourites() else playerViewModel.shuffleAll() }) {
            Icon(imageVector = Icons.Default.Shuffle, contentDescription = "Shuffle")
        }
    }) {
        SongListView(showFavourites)
    }
}