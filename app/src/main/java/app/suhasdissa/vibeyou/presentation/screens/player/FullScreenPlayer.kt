package app.suhasdissa.vibeyou.presentation.screens.player

import android.text.format.DateUtils
import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOn
import androidx.compose.material.icons.rounded.RepeatOneOn
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.PlayerRepeatMode
import app.suhasdissa.vibeyou.backend.models.PlayerState
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.IS_LOCAL_KEY
import app.suhasdissa.vibeyou.utils.isPlayingState
import app.suhasdissa.vibeyou.utils.maxResThumbnail
import app.suhasdissa.vibeyou.utils.mediaItemState
import app.suhasdissa.vibeyou.utils.positionAndDurationState
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPlayer(
    controller: MediaController,
    onCollapse: (() -> Unit)?,
    playerViewModel: PlayerViewModel,
    onClickShowSongOptions: () -> Unit,
    onClickShowQueueSheet: () -> Unit
) {
    val view = LocalView.current
    CenterAlignedTopAppBar(navigationIcon = {
        onCollapse?.let { onCollapse ->
            IconButton({
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onCollapse.invoke()
            }) {
                Icon(
                    Icons.Rounded.ExpandMore,
                    contentDescription = stringResource(R.string.close_player)
                )
            }
        }
    }, title = { Text(stringResource(R.string.now_playing)) }, actions = {
        IconButton(onClick = onClickShowSongOptions) {
            Icon(Icons.Rounded.MoreVert, contentDescription = stringResource(R.string.song_options))
        }
    })
    HorizontalDivider(Modifier.fillMaxWidth())
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

            AlbumArtBox(
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                it
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
                onClickShowQueueSheet.invoke()
            }) {
                Icon(Icons.AutoMirrored.Rounded.QueueMusic, stringResource(R.string.show_queue))
            }
        }
    }
}

@Composable
fun FullScreenPlayerHorizontal(
    controller: MediaController,
    playerViewModel: PlayerViewModel,
    onClickShowQueueSheet: () -> Unit
) {
    val view = LocalView.current
    Row(
        Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
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
            AlbumArtBox(
                modifier = Modifier
                    .size(250.dp)
                    .padding(16.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                it
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
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
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onClickShowQueueSheet.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.QueueMusic,
                            stringResource(R.string.show_queue)
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun AlbumArtBox(
    modifier: Modifier = Modifier,
    mediaItem: MediaItem
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = mediaItem.maxResThumbnail,
        contentDescription = stringResource(id = R.string.album_art),
        contentScale = ContentScale.Crop
    ) {
        val maxResPainterState = painter.state
        if (maxResPainterState !is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImage(
                model = mediaItem.mediaMetadata.artworkUri,
                contentDescription,
                contentScale = contentScale
            ) {
                val lowResPainterState = painter.state
                if (lowResPainterState !is AsyncImagePainter.State.Success) {
                    Image(
                        painter = painterResource(id = R.drawable.music_placeholder),
                        contentDescription,
                        contentScale = contentScale
                    )
                } else {
                    SubcomposeAsyncImageContent(contentScale = contentScale)
                }
            }
        } else {
            SubcomposeAsyncImageContent(contentScale = contentScale)
        }
    }
}

@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    isfavourite: MutableState<Boolean>,
    isLocal: Boolean,
    onToggleFavourite: () -> Unit
) {
    val view = LocalView.current
    playerViewModel.controller?.let { controller ->
        Column(modifier.padding(32.dp)) {
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
                        Icon(Icons.Rounded.Favorite, contentDescription = null)
                    } else {
                        Icon(Icons.Rounded.FavoriteBorder, contentDescription = null)
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
                        Icon(Icons.Rounded.SkipPrevious, contentDescription = null)
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
                                    Icons.Rounded.Pause,
                                    modifier = Modifier.size(36.dp),
                                    contentDescription = stringResource(R.string.pause)
                                )
                            }

                            PlayerState.Pause -> {
                                Icon(
                                    Icons.Rounded.PlayArrow,
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
                        Icon(Icons.Rounded.SkipNext, contentDescription = null)
                    }
                }
                var repeatState by remember {
                    mutableStateOf(PlayerRepeatMode.entries[controller.repeatMode])
                }

                when (repeatState) {
                    PlayerRepeatMode.OFF -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.ALL
                            controller.repeatMode = PlayerRepeatMode.ALL.mode
                        }) {
                            Icon(
                                Icons.Rounded.Repeat,
                                contentDescription = stringResource(R.string.repeat_off)
                            )
                        }
                    }

                    PlayerRepeatMode.ALL -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.ONE
                            controller.repeatMode = PlayerRepeatMode.ONE.mode
                        }) {
                            Icon(
                                Icons.Rounded.RepeatOn,
                                contentDescription = stringResource(R.string.repeat_all)
                            )
                        }
                    }

                    PlayerRepeatMode.ONE -> {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            repeatState = PlayerRepeatMode.OFF
                            controller.repeatMode = PlayerRepeatMode.OFF.mode
                        }) {
                            Icon(
                                Icons.Rounded.RepeatOneOn,
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
