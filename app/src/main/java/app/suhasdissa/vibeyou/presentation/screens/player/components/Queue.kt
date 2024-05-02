package app.suhasdissa.vibeyou.presentation.screens.player.components

import android.view.SoundEffectConstants
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.components.SongCardCompact
import app.suhasdissa.vibeyou.utils.DisposableListener
import app.suhasdissa.vibeyou.utils.queue
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Queue(
    controller: MediaController
) {
    var queueItems by remember { mutableStateOf(controller.currentTimeline.queue) }
    var currentItemIndex by remember { mutableIntStateOf(controller.currentMediaItemIndex) }
    controller.DisposableListener {
        object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentItemIndex = controller.currentMediaItemIndex
            }
        }
    }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        queueItems = queueItems.toMutableList().apply {
            val removedItem = removeAt(from.index)
            add(to.index, removedItem)
        }
    }, onDragEnd = { from, to ->
        controller.moveMediaItem(from, to)
    })
    val view = LocalView.current
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
    ) {
        items(items = queueItems, { it.first }) { queue ->
            val mediaItem = queue.second
            ReorderableItem(reorderableState = state, key = queue.first) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Modifier
                    .shadow(elevation)
                    .detectReorderAfterLongPress(state)
                    .background(MaterialTheme.colorScheme.surface)
                SongCardCompact(
                    thumbnail = mediaItem.mediaMetadata.artworkUri,
                    title = mediaItem.mediaMetadata.title.toString(),
                    artist = mediaItem.mediaMetadata.artist.toString(),
                    active = queue.first == currentItemIndex,
                    inactive = queue.first < currentItemIndex,
                    TrailingContent = {
                        IconButton(onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            controller.removeMediaItem(queue.first)
                            queueItems = queueItems.toMutableList().apply { remove(queue) }
                        }) {
                            Icon(
                                Icons.Rounded.Clear,
                                stringResource(R.string.remove_item_from_queue)
                            )
                        }
                    },
                    onClickVideoCard = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        controller.seekTo(queue.first, 0L)
                    },
                    modifier = Modifier.animateItem(
                        fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(
                            durationMillis = 100,
                            easing = FastOutSlowInEasing
                        )
                    )
                )
            }
        }
    }
}
