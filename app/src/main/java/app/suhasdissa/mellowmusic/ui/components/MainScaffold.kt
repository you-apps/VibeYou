package app.suhasdissa.mellowmusic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.ui.screens.player.FullScreenPlayer
import app.suhasdissa.mellowmusic.ui.screens.player.MiniPlayer
import app.suhasdissa.mellowmusic.utils.mediaItemState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    fab: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
    var isPlayerSheetVisible by remember { mutableStateOf(false) }
    val playerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    if (isPlayerSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isPlayerSheetVisible = false },
            sheetState = playerSheetState,
            shape = RoundedCornerShape(8.dp),
            dragHandle = null
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    IconButton(onClick = {
                        scope.launch { playerSheetState.hide() }.invokeOnCompletion {
                            if (!playerSheetState.isVisible) {
                                isPlayerSheetVisible = false
                            }
                        }
                    }) {
                        Icon(Icons.Default.ExpandMore, contentDescription = "Close Player")
                    }
                    Text(text = "Now Playing", style = MaterialTheme.typography.titleLarge)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.MoreVert, "Song Options")
                }
            }
            Divider(Modifier.fillMaxWidth())
            playerViewModel.controller?.let { controller ->
                FullScreenPlayer(
                    controller,
                    onToggleFavourite = { playerViewModel.toggleFavourite(it) })
            }

        }
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
                        onClick = {
                            isPlayerSheetVisible = true
                            scope.launch {
                                playerSheetState.show()
                            }
                        },
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