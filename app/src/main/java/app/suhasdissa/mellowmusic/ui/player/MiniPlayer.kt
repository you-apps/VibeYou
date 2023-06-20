package app.suhasdissa.mellowmusic.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.utils.isPlayingState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun MiniPlayer(
    onClick: () -> Unit,
    controller: MediaController,
    mediaItem: MediaItem,
    onPlayPause: () -> Unit,
    onSeekNext: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(mediaItem.mediaMetadata.artworkUri).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        val title = mediaItem.mediaMetadata.title.toString()
        val artist = mediaItem.mediaMetadata.artist.toString()
        Column(
            Modifier.weight(1f),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                artist,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            val playState by controller.isPlayingState()
            IconButton(onClick = { onPlayPause() }) {
                if (playState) {
                    Icon(Icons.Default.Pause, contentDescription = null)
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                }
            }
            IconButton(onClick = { onSeekNext() }) {
                Icon(Icons.Default.SkipNext, contentDescription = null)
            }

        }
    }
}