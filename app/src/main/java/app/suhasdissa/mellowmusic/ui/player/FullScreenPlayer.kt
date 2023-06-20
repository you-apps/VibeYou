package app.suhasdissa.mellowmusic.ui.player

import android.text.format.DateUtils
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.utils.isPlayingState
import app.suhasdissa.mellowmusic.utils.maxResThumbnail
import app.suhasdissa.mellowmusic.utils.mediaItemState
import app.suhasdissa.mellowmusic.utils.positionAndDurationState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun FullScreenPlayer(
    controller: MediaController, onToggleFavourite: (id: String) -> Unit
) {
    var showQueueSheet by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        val mediaItem by controller.mediaItemState()
        mediaItem?.let {
            AsyncImage(
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(it.maxResThumbnail).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            val title = it.mediaMetadata.title.toString()
            val artist = it.mediaMetadata.artist.toString()

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Text(artist, style = MaterialTheme.typography.titleMedium)
            }
            PlayerController(
                isfavourite = it.mediaMetadata.extras?.getBoolean("isFavourite") ?: false,
                onToggleFavourite = { onToggleFavourite(it.mediaId) })
        }
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { showQueueSheet = true }) {
                Icon(Icons.Default.QueueMusic,"Show Queue")
            }
        }
    }
    if (showQueueSheet) QueueSheet(onDismissRequest = { showQueueSheet = false })
}

@Composable
fun PlayerController(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    isfavourite: Boolean,
    onToggleFavourite: () -> Unit
) {
    playerViewModel.controller?.let { controller ->
        Column(Modifier.padding(32.dp)) {
            val positionAndDuration by controller.positionAndDurationState()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(DateUtils.formatElapsedTime(positionAndDuration.first / 1000))
                var tempSliderPosition by remember { mutableStateOf<Float?>(null) }
                Slider(modifier = Modifier.weight(1f),
                    value = tempSliderPosition ?: positionAndDuration.first.toFloat(),
                    onValueChange = { tempSliderPosition = it },
                    valueRange = 0f.rangeTo(
                        positionAndDuration.second?.toFloat() ?: Float.MAX_VALUE
                    ),
                    onValueChangeFinished = {
                        tempSliderPosition?.let {
                            playerViewModel.seekTo(it.toLong())
                        }
                        tempSliderPosition = null
                    }
                )
                Text(positionAndDuration.second?.let { DateUtils.formatElapsedTime(it / 1000) }
                    ?: "")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var favouriteState by remember { mutableStateOf(isfavourite) }
                IconButton(onClick = {
                    onToggleFavourite()
                    favouriteState = !favouriteState
                }) {
                    if (favouriteState) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                    } else {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    }
                }
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
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ), shape = CircleShape
                ) {
                    val playState by controller.isPlayingState()
                    IconButton(
                        onClick = { playerViewModel.playPause() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (playState) {
                            Icon(
                                Icons.Default.Pause,
                                modifier = Modifier.size(36.dp),
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                Icons.Default.PlayArrow,
                                modifier = Modifier.size(36.dp),
                                contentDescription = null
                            )
                        }
                    }
                }
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
                var shuffleState by remember { mutableStateOf(false) }
                IconButton(onClick = { shuffleState = !shuffleState }) {
                    if (shuffleState) {
                        Icon(Icons.Default.ShuffleOn, contentDescription = null)
                    } else {
                        Icon(Icons.Default.Shuffle, contentDescription = null)
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerControllerPreview() {
    PlayerController(isfavourite = false, onToggleFavourite = {})
}