package app.suhasdissa.mellowmusic.ui.screens.songs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel

@Composable
fun SongsScreen(
    showFavourites: Boolean,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { if (showFavourites) playerViewModel.shuffleFavourites() else playerViewModel.shuffleAll() }) {
            Icon(imageVector = Icons.Default.Shuffle, contentDescription = stringResource(R.string.shuffle))
        }
    }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            SongListView(showFavourites)
        }
    }
}