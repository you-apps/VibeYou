package app.suhasdissa.mellowmusic.ui.screens.player

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.ui.components.SongCardCompact
import app.suhasdissa.mellowmusic.utils.queue
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
    // This code seems to break the drag and drop animation
    /*
    controller.DisposableListener {
        object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                queueItems.clear()
                queueItems.addAll(timeline.queue)
            }
        }
    }
     */
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        queueItems = queueItems.toMutableList().apply {
            val removedItem = removeAt(from.index)
            add(to.index, removedItem)
        }
    }, onDragEnd = { from, to ->
        controller.moveMediaItem(from,to)
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
    ) {
        items(items = queueItems, { it.first }) { queue ->
            val mediaItem = queue.second
            ReorderableItem(reorderableState = state, key = queue.first) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp)
                SongCardCompact(
                    thumbnail = mediaItem.mediaMetadata.artworkUri,
                    title = mediaItem.mediaMetadata.title.toString(),
                    artist = mediaItem.mediaMetadata.artist.toString(),
                    TrailingContent = {
                        IconButton(onClick = {
                            controller.removeMediaItem(queue.first)
                            queueItems = queueItems.toMutableList().apply { remove(queue) }
                        }) {
                            Icon(Icons.Default.Clear, null)
                        }
                    },
                    onClickVideoCard = { controller.seekTo(queue.first, 0L) },
                    modifier = Modifier
                        .shadow(elevation)
                        .detectReorderAfterLongPress(state)
                        .background(MaterialTheme.colorScheme.surface)
                        .animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 100,
                                easing = FastOutSlowInEasing
                            )
                        )
                )
            }
        }
    }
}