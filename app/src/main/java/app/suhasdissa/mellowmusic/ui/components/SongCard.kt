package app.suhasdissa.mellowmusic.ui.components

import android.net.Uri
import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongCard(
    thumbnail: String?,
    title: String,
    artist: String?,
    duration: String?,
    onClickCard: () -> Unit,
    onLongPress: () -> Unit
) {
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    Row(
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onClickCard()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongPress()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(thumbnail).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            artist?.let {
                Text(
                    artist,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        duration?.let {
            Text(
                duration,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SongCardCompact(
    thumbnail: Uri?,
    title: String,
    artist: String?,
    onClickVideoCard: () -> Unit,
    TrailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { onClickVideoCard() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(thumbnail).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            artist?.let {
                Text(
                    artist,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            Modifier
                .padding(8.dp)
        ) {
            TrailingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SongCardPreview() {
    SongCard(
        thumbnail = "",
        title = "Song Name",
        artist = "Artist Name",
        duration = "8.37",
        onClickCard = {},
        onLongPress = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SongCardCompactPreview() {
    SongCardCompact(
        thumbnail = Uri.EMPTY,
        title = "Song Name",
        artist = "Artist Name",
        TrailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icons.Default.PlayArrow
            }
        },
        onClickVideoCard = {}
    )
}
