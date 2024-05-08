package app.suhasdissa.vibeyou.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.presentation.screens.player.FullScreenPlayer
import app.suhasdissa.vibeyou.presentation.screens.player.FullScreenPlayerHorizontal
import app.suhasdissa.vibeyou.presentation.screens.player.MiniPlayer
import app.suhasdissa.vibeyou.presentation.screens.player.components.EqualizerSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.QueueSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.SongOptionsSheet
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.mediaItemState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniPlayerScaffold(
    playerViewModel: PlayerViewModel,
    horizontalPlayer: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    var isPlayerSheetVisible by remember { mutableStateOf(false) }
    var showQueueSheet by remember { mutableStateOf(false) }
    var showSongOptions by remember { mutableStateOf(false) }
    var showEqualizerSheet by remember {
        mutableStateOf(false)
    }
    val playerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    if (isPlayerSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isPlayerSheetVisible = false },
            sheetState = playerSheetState,
            shape = RoundedCornerShape(8.dp),
            dragHandle = null,
            sheetMaxWidth = Dp.Unspecified,
            windowInsets = if (horizontalPlayer) WindowInsets(
                0,
                0,
                0,
                0
            ) else BottomSheetDefaults.windowInsets
        ) {
            playerViewModel.controller?.let { controller ->
                if (horizontalPlayer) {
                    Row(Modifier.fillMaxSize()) {
                        FullScreenPlayerHorizontal(
                            controller,
                            playerViewModel,
                            onClickShowQueueSheet = { showQueueSheet = true }
                        )
                    }
                } else {
                    FullScreenPlayer(
                        controller,
                        onCollapse = {
                            scope.launch { playerSheetState.hide() }.invokeOnCompletion {
                                if (!playerSheetState.isVisible) {
                                    isPlayerSheetVisible = false
                                }
                            }
                        },
                        playerViewModel,
                        onClickShowQueueSheet = { showQueueSheet = true },
                        onClickShowSongOptions = { showSongOptions = true }
                    )
                }
            }
        }
    }
    if (showQueueSheet) QueueSheet(onDismissRequest = { showQueueSheet = false }, playerViewModel)
    if (showSongOptions) SongOptionsSheet(
        onDismissRequest = { showSongOptions = false },
        playerViewModel,
        onClickShowEqualizer = { showEqualizerSheet = true }
    )
    if (showEqualizerSheet) {
        val app = LocalContext.current.applicationContext as MellowMusicApplication
        EqualizerSheet(
            equalizerData = app.supportedEqualizerData!!,
            onDismissRequest = { showEqualizerSheet = false }, playerViewModel = playerViewModel
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        playerViewModel
                    )
                }
            }
        }, content = content, contentWindowInsets = WindowInsets.systemBars
    )
}
