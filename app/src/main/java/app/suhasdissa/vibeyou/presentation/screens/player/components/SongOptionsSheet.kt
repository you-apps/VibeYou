package app.suhasdissa.vibeyou.presentation.screens.player.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOptionsSheet(
    onDismissRequest: () -> Unit,
    playerViewModel: PlayerViewModel,
    onClickShowEqualizer: () -> Unit
) {
    val playerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val app = LocalContext.current.applicationContext as MellowMusicApplication

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
                    Icons.Rounded.ExpandMore, contentDescription = null
                )
            }
        }, title = { Text(stringResource(R.string.song_options)) })
        HorizontalDivider(Modifier.fillMaxWidth())

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            var speed by remember {
                mutableFloatStateOf(playerViewModel.getPlaybackParams().speed)
            }
            var pitch by remember {
                mutableFloatStateOf(playerViewModel.getPlaybackParams().pitch)
            }

            fun updatePlaybackParams() = playerViewModel.setPlaybackParams(speed, pitch)

            Text(text = stringResource(R.string.playback_speed), fontSize = 16.sp)
            Slider(value = speed, onValueChange = {
                speed = it
                updatePlaybackParams()
            }, valueRange = 0.25f..4f, steps = 14)
            ElevatedCard(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    text = "%.2f".format(speed)
                )
            }
            Text(text = stringResource(R.string.pitch), fontSize = 16.sp)
            Slider(value = pitch, onValueChange = {
                pitch = it
                updatePlaybackParams()
            }, valueRange = 0.5f..2f, steps = 5)
            ElevatedCard(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    text = "%.2f".format(pitch)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (app.supportedEqualizerData != null) {
            Button(modifier = Modifier.padding(start = 10.dp), onClick = {
                onClickShowEqualizer.invoke()
            }) {
                Text(text = stringResource(id = R.string.equalizer))
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}
