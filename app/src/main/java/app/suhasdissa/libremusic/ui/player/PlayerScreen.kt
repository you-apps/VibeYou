package app.suhasdissa.libremusic.ui.player

import android.text.format.DateUtils
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.libremusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.libremusic.utils.mediaItemState
import app.suhasdissa.libremusic.utils.positionAndDurationState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PlayerScreen(
    onPlayerClose: () -> Unit,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    Column(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 120) {
                        change.consume()
                        //onPlayerClose()
                    }
                }
            },
        verticalArrangement = Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        val mediaItem by playerViewModel.controller.mediaItemState()
        mediaItem?.let {
            AsyncImage(
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(it.mediaMetadata.artworkUri).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            val title = it.mediaMetadata.title.toString()
            val artist = it.mediaMetadata.artist.toString()

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Text(artist, style = MaterialTheme.typography.titleMedium)
            }
        }
        PlayerController()
    }
}

@Composable
fun PlayerController(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
) {

    Column(Modifier.padding(32.dp)) {
        val positionAndDuration by playerViewModel.controller.positionAndDurationState()
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(DateUtils.formatElapsedTime(positionAndDuration.first / 1000))
            var tempSliderPosition by remember { mutableStateOf<Float?>(null) }
            Slider(modifier = Modifier.weight(1f),
                value = tempSliderPosition ?: positionAndDuration.first.toFloat(),
                onValueChange = { tempSliderPosition = it },
                valueRange = 0f.rangeTo(positionAndDuration.second?.toFloat() ?: Float.MAX_VALUE),
                onValueChangeFinished = {
                    tempSliderPosition?.let {
                        playerViewModel.seekTo(it.toLong())
                    }
                    tempSliderPosition = null
                }
            )
            Text(positionAndDuration.second?.let { DateUtils.formatElapsedTime(it / 1000) } ?: "")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                IconButton(onClick = { playerViewModel.seekPrevious() }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                IconButton(onClick = { playerViewModel.playPause() }) {
                    if (true) {
                        Icon(Icons.Default.Pause, contentDescription = null)
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                IconButton(onClick = { playerViewModel.seekNext() }) {
                    Icon(Icons.Default.SkipNext, contentDescription = null)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PlayerControllerPreview() {
    PlayerController()
}