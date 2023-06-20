package app.suhasdissa.mellowmusic.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
fun Player.positionAndDurationState(): State<Pair<Long, Long?>> {
    return produceState(
        initialValue = (currentPosition to duration.let { if (it < 0) null else it }),
        this
    ) {
        var isSeeking = false
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isSeeking = false
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                value = currentPosition to value.second
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    isSeeking = true
                    value = currentPosition to duration.let { if (it < 0) null else it }
                }
            }
        }
        addListener(listener)

        val pollJob = launch {
            while (isActive) {
                delay(1000)
                if (!isSeeking) {
                    value = currentPosition to duration.let { if (it < 0) null else it }
                }
            }
        }
        if (!isActive) {
            pollJob.cancel()
            removeListener(listener)
        }
    }
}

@Composable
fun Player.mediaItemState(): State<MediaItem?> {
    return produceState(initialValue = currentMediaItem, this) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                value = mediaItem
            }
        }
        addListener(listener)
        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            removeListener(listener)
        }
    }
}

@Composable
fun Player.isPlayingState(): State<Boolean> {
    return produceState(initialValue = isPlaying, this) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                value = isPlaying
            }
        }
        addListener(listener)
        if (!isActive) {
            removeListener(listener)
        }
    }
}

@Composable
inline fun Player.DisposableListener(crossinline listenerProvider: () -> Player.Listener) {
    DisposableEffect(this) {
        val listener = listenerProvider()
        addListener(listener)
        onDispose { removeListener(listener) }
    }
}