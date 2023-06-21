package app.suhasdissa.mellowmusic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.ui.screens.player.MiniPlayer
import app.suhasdissa.mellowmusic.ui.screens.player.PlayerSheet
import app.suhasdissa.mellowmusic.utils.mediaItemState

@Composable
fun MainScaffold(
    fab: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
    var isPlayerSheetVisible by remember { mutableStateOf(false) }
    if (isPlayerSheetVisible) {
        PlayerSheet(onDismissRequest = { isPlayerSheetVisible = false })
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = fab,
        topBar = topBar,
        bottomBar = {
            playerViewModel.controller?.let { controller ->
                val mediaItem by controller.mediaItemState()
                mediaItem?.let {
                    MiniPlayer(
                        onClick = { isPlayerSheetVisible = true },
                        controller,
                        it,
                        onPlayPause = { playerViewModel.playPause() },
                        onSeekNext = { playerViewModel.seekNext() })
                }
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            content()
        }
    }
}