package app.suhasdissa.vibeyou.presentation.screens.onlinemusic.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.components.SongListView
import app.suhasdissa.vibeyou.presentation.screens.onlinemusic.model.SongViewModel
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.asSong

@Composable
fun SongsScreen(
    showFavourites: Boolean,
    playerViewModel: PlayerViewModel,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
) {
    val view = LocalView.current
    val songs =
        (if (showFavourites) songViewModel.favSongs.collectAsState() else songViewModel.songs.collectAsState()).value.map {
            it.asSong
        }
    Scaffold(floatingActionButton = {
        Row {
            FloatingActionButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                playerViewModel.playSongs(songs)
            }) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = stringResource(R.string.play_all)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            FloatingActionButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                playerViewModel.playSongs(songs, shuffle = true)
            }) {
                Icon(
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = stringResource(R.string.shuffle)
                )
            }
        }
    }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SongListView(songs, playerViewModel)
        }
    }
}
