package app.suhasdissa.libremusic.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
fun Player.positionAndDurationState(): State<Pair<Long, Long?>> {
    val state = remember {
        mutableStateOf(currentPosition to duration.let { if (it < 0) null else it })
    }

    LaunchedEffect(this) {
        var isSeeking = false

        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isSeeking = false
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                state.value = currentPosition to state.value.second
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    isSeeking = true
                    state.value = currentPosition to duration.let { if (it < 0) null else it }
                }
            }
        }

        addListener(listener)

        val pollJob = launch {
            while (isActive) {
                delay(1000)
                if (!isSeeking) {
                    state.value = currentPosition to duration.let { if (it < 0) null else it }
                }
            }
        }

        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            pollJob.cancel()
            removeListener(listener)
        }
    }

    return state
}

@Composable
fun Player.mediaItemState(): State<MediaItem?> {
    val state = remember {
        mutableStateOf(currentMediaItem)
    }

    LaunchedEffect(this) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                state.value = mediaItem
            }
        }
        addListener(listener)
        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            removeListener(listener)
        }
    }

    return state
}

@Composable
fun Player.isPlayingState(): State<Boolean> {
    val state = remember {
        mutableStateOf(isPlaying)
    }
    LaunchedEffect(this) {
        val listener = object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                state.value = isPlaying
            }
        }
        addListener(listener)
        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            removeListener(listener)
        }
    }

    return state
}