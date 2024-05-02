package app.suhasdissa.vibeyou.presentation.components

import android.net.Uri
import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Song
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongCard(
    song: Song,
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
            model = song.thumbnailUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.music_placeholder)
        )
        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                song.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            song.artistsText?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        song.durationText?.let {
            Text(
                it,
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
    active: Boolean = false,
    inactive: Boolean = false,
    onClickVideoCard: () -> Unit,
    TrailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .alpha(if (inactive) 0.5f else 1f)
            .clickable { onClickVideoCard() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (active) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    Icons.Rounded.BarChart,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        } else {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.music_placeholder)
            )
        }
        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
            artist?.let {
                Text(
                    artist,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
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
        Song("", "Title", "Artist", "0:00"),
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
                Icons.Rounded.PlayArrow
            }
        },
        onClickVideoCard = {}
    )
}
