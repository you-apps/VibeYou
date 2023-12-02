package app.suhasdissa.vibeyou.ui.screens.player

import android.text.format.DateUtils
import android.view.SoundEffectConstants
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
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.PlayerRepeatMode
import app.suhasdissa.vibeyou.backend.models.PlayerState
import app.suhasdissa.vibeyou.backend.viewmodel.PlayerViewModel
import app.suhasdissa.vibeyou.utils.IS_LOCAL_KEY
import app.suhasdissa.vibeyou.utils.isPlayingState
import app.suhasdissa.vibeyou.utils.maxResThumbnail
import app.suhasdissa.vibeyou.utils.mediaItemState
import app.suhasdissa.vibeyou.utils.positionAndDurationState
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPlayer(
    controller: MediaController,
    onCollapse: () -> Unit,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    var showQueueSheet by remember { mutableStateOf(false) }
    var showSongOptions by remember { mutableStateOf(false) }

    val view = LocalView.current
    CenterAlignedTopAppBar(navigationIcon = {
        IconButton({
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onCollapse.invoke()
        }) {
            Icon(
                Icons.Rounded.ExpandMore,
                contentDescription = stringResource(R.string.close_player)
            )
        }
    }, title = { Text(stringResource(R.string.now_playing)) }, actions = {
        IconButton(onClick = { showSongOptions = true }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = stringResource(R.string.song_options))
        }
    })
    Divider(Modifier.fillMaxWidth())
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        val mediaItem by controller.mediaItemState()
        val isFavourite = remember {
            mutableStateOf(false)
        }
        var isLocal by remember {
            mutableStateOf(false)
        }
        mediaItem?.let {
            LaunchedEffect(mediaItem) {
                isLocal = mediaItem!!.mediaMetadata.extras?.getBoolean(IS_LOCAL_KEY) ?: false
                if (!isLocal) {
                    isFavourite.value = playerViewModel.isFavourite(mediaItem!!.mediaId)
                }
            }
            var thumbnailUrl by remember {
                mutableStateOf(it.maxResThumbnail)
            }

            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                model = it.maxResThumbnail.ifEmpty { it.mediaMetadata.artworkUri },
                contentDescription = stringResource(id = R.string.album_art),
                contentScale = ContentScale.Crop,
                onError = { _ ->
                    if (thumbnailUrl != it.mediaMetadata.artworkUri.toString()) {
                        thumbnailUrl = it.mediaMetadata.artworkUri.toString()
                    }
                },
                error = {
                    SubcomposeAsyncImageContent(
                        painter = painterResource(id = R.drawable.music_placeholder),
                        contentScale = contentScale
                    )
                }
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
                Text(
                    artist,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            PlayerController(
                isfavourite = isFavourite,
                isLocal = isLocal,
                onToggleFavourite = { playerViewModel.toggleFavourite(it.mediaId) }
            )
        }
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showQueueSheet = true
            }) {
                Icon(Icons.Default.QueueMusic, stringResource(R.string.show_queue))
            }
        }
    }
    if (showQueueSheet) QueueSheet(onDismissRequest = { showQueueSheet = false })
    if (showSongOptions) SongOptionsSheet(onDismissRequest = { showSongOptions = false })
}

@Composable
fun PlayerController(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    isfavourite: MutableState<Boolean>,
    isLocal: Boolean,
    onToggleFavourite: () -> Unit
) {
    val view = LocalView.current
    playerViewModel.controller?.let { controller ->
        Column(Modifier.padding(32.dp)) {
            val positionAndDuration by controller.positionAndDurationState()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(DateUtils.formatElapsedTime(positionAndDuration.first / 1000))
                var tempSliderPosition by remember { mutableStateOf<Float?>(null) }
                Slider(
                    modifier = Modifier.weight(1f),
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
                Text(
                    positionAndDuration.second?.let { DateUtils.formatElapsedTime(it / 1000) }
                        ?: ""
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var favouriteState by isfavourite
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onToggleFavourite()
                    favouriteState = !favouriteState
                }, enabled = !isLocal) {
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
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        playerViewModel.seekPrevious()
                    }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = null)
                    }
                }
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    shape = CircleShape
                ) {
                    val playState by controller.isPlayingState()
                    IconButton(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            playerViewModel.playPause()
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        when (playState) {
                            PlayerState.Buffer -> {
                                CircularProgressIndicator(modifier = Modifier.size(36.dp))
                            }

                            PlayerState.Play -> {
                                Icon(
                                    Icons.Default.Pause,
                                    modifier = Modifier.size(36.dp),
                                    contentDescription = stringResource(R.string.pause)
                                )
                            }

                            PlayerState.Pause -> {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    modifier = Modifier.size(36.dp),
                                    contentDescription = stringResource(R.string.play)
                                )
                            }
                        }
                    }
                }
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        playerViewModel.seekNext()
                    }) {
                        Icon(Icons.Default.SkipNext, contentDescription = null)
                    }
                }
                var repeatState by remember {
                    mutableStateOf(PlayerRepeatMode.values()[controller.repeatMode])
                }

                when (repeatState) {
                    PlayerRepeatMode.OFF -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.ALL
                            controller.repeatMode = repeatState.mode
                        }) {
                            Icon(
                                Icons.Default.Repeat,
                                contentDescription = stringResource(R.string.repeat_off)
                            )
                        }
                    }

                    PlayerRepeatMode.ALL -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.ONE
                            controller.repeatMode = repeatState.mode
                        }) {
                            Icon(
                                Icons.Default.RepeatOn,
                                contentDescription = stringResource(R.string.repeat_all)
                            )
                        }
                    }

                    PlayerRepeatMode.ONE -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.OFF
                            controller.repeatMode = repeatState.mode
                        }) {
                            Icon(
                                Icons.Default.RepeatOneOn,
                                contentDescription = stringResource(R.string.repeat_one)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerControllerPreview() {
    PlayerController(
        isfavourite = remember { mutableStateOf(false) },
        isLocal = false,
        onToggleFavourite = {}
    )
}
