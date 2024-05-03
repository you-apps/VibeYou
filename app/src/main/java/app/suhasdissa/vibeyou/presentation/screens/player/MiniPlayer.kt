package app.suhasdissa.vibeyou.presentation.screens.player

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.PlayerState
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.isPlayingState
import app.suhasdissa.vibeyou.utils.positionAndDurationState
import coil.compose.AsyncImage

@Composable
fun MiniPlayer(
    onClick: () -> Unit,
    controller: MediaController,
    mediaItem: MediaItem,
    playerViewModel: PlayerViewModel,
    windowInsets: WindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
) {
    val view = LocalView.current
    Row(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = mediaItem.mediaMetadata.artworkUri,
            contentDescription = stringResource(R.string.song_album_art),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.music_placeholder)
        )
        val title = mediaItem.mediaMetadata.title.toString()
        val artist = mediaItem.mediaMetadata.artist.toString()
        Column(
            Modifier.weight(1f)
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
            IconButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                playerViewModel.playPause()
            }) {
                when (playState) {
                    PlayerState.Buffer -> {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }

                    PlayerState.Play -> {
                        Icon(
                            Icons.Rounded.Pause,
                            contentDescription = stringResource(R.string.pause)
                        )
                    }

                    PlayerState.Pause -> {
                        Icon(
                            Icons.Rounded.PlayArrow,
                            contentDescription = stringResource(R.string.play)
                        )
                    }
                }
            }
            IconButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                playerViewModel.seekNext()
            }) {
                Icon(
                    Icons.Rounded.SkipNext,
                    contentDescription = stringResource(R.string.skip_next)
                )
            }
        }
    }
    val positionAndDuration by controller.positionAndDurationState()
    val progress =
        positionAndDuration.second?.let { duration ->
            positionAndDuration.first.toFloat().div(duration)
        }
            ?: 0f
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
    )
}
