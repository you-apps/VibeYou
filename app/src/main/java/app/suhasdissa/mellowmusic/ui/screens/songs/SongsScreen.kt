package app.suhasdissa.mellowmusic.ui.screens.songs

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.SongViewModel

@Composable
fun SongsScreen(
    showFavourites: Boolean,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
) {
    val view = LocalView.current
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            if (showFavourites) playerViewModel.shuffleFavourites() else playerViewModel.shuffleAll()
        }) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = stringResource(R.string.shuffle)
            )
        }
    }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val songs by if (showFavourites) songViewModel.favSongs.collectAsState() else songViewModel.songs.collectAsState()
            SongListView(songs)
        }
    }
}
