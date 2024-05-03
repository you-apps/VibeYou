package app.suhasdissa.vibeyou.presentation.screens.player.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueSheet(
    onDismissRequest: () -> Unit,
    playerViewModel: PlayerViewModel
) {
    val playerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = playerSheetState,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 0.dp,
        dragHandle = null
    ) {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton({
                view.playSoundEffect(SoundEffectConstants.CLICK)
                scope.launch {
                    playerSheetState.hide()
                }.invokeOnCompletion {
                    onDismissRequest()
                }
            }) {
                Icon(
                    Icons.Rounded.ExpandMore,
                    contentDescription = stringResource(R.string.close_queue)
                )
            }
        }, title = { Text(stringResource(R.string.player_queue)) }, actions = {
            var showDropdown by remember { mutableStateOf(false) }
            IconButton({
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showDropdown = true
            }) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = stringResource(R.string.close_queue)
                )
            }
            DropdownMenu(expanded = showDropdown, onDismissRequest = { showDropdown = false }) {
                DropdownMenuItem(text = {
                    Text(text = stringResource(R.string.clear_queue))
                }, onClick = {
                    playerViewModel.controller?.clearMediaItems()
                    scope.launch {
                        playerSheetState.hide()
                    }.invokeOnCompletion {
                        onDismissRequest()
                    }
                })
            }
        })
        HorizontalDivider(Modifier.fillMaxWidth())
        playerViewModel.controller?.let { controller ->
            Queue(controller)
        }
    }
}
