package app.suhasdissa.libremusic.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.libremusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.libremusic.utils.isPlayingState
import app.suhasdissa.libremusic.utils.mediaItemState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun MiniPlayer(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    val isPlaying by playerViewModel.controller.isPlayingState()
    AnimatedVisibility(isPlaying) {
        Row(
            Modifier.height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val mediaItem by playerViewModel.controller.mediaItemState()
            mediaItem?.let {
                AsyncImage(
                    modifier = Modifier
                        .size(84.dp)
                        .padding(8.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(it.mediaMetadata.artworkUri).crossfade(true).build(),
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
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(artist, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { playerViewModel.seekPrevious() }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { playerViewModel.playPause() }) {
                    if (true) {
                        Icon(Icons.Default.Pause, contentDescription = null)
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { playerViewModel.seekNext() }) {
                    Icon(Icons.Default.SkipNext, contentDescription = null)
                }

            }
        }
    }
}