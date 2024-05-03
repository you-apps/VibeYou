package app.suhasdissa.vibeyou.utils

import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline

inline val Timeline.windows: List<Timeline.Window>
    get() = List(windowCount) {
        getWindow(it, Timeline.Window())
    }

inline val Timeline.queue: List<Pair<Int, MediaItem>>
    get() = List(windowCount) {
        it to getWindow(it, Timeline.Window()).mediaItem
    }

inline val Timeline.mediaIdList: List<String>
    get() = List(windowCount) {
        getWindow(it, Timeline.Window()).mediaItem.mediaId
    }

fun Player.playPause() {
    if (isPlaying) {
        pause()
    } else {
        if (playbackState == Player.STATE_IDLE) {
            prepare()
        }
        play()
    }
}

fun Player.forcePlay(mediaItem: MediaItem) {
    setMediaItem(mediaItem, true)
    playWhenReady = true
    prepare()
}

fun Player.playGracefully(mediaItem: MediaItem) {
    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
        forcePlay(mediaItem)
    } else {
        val newIndex = currentPeriodIndex + 1
        addMediaItem(newIndex, mediaItem)
        seekTo(newIndex, C.TIME_UNSET)
    }
}

fun Player.seekNext() {
    if (playbackState == Player.STATE_IDLE) {
        prepare()
    }
    seekToNext()

}

fun Player.seek(position: Long) {
    if (playbackState == Player.STATE_IDLE) {
        prepare()
    }
    seekTo(position)
}

fun Player.enqueue(mediaItem: MediaItem) {
    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
        forcePlay(mediaItem)
    } else {
        addMediaItem(mediaItemCount, mediaItem)
    }
}

fun Player.forcePlayAtIndex(mediaItems: List<MediaItem>, mediaItemIndex: Int) {
    if (mediaItems.isEmpty()) return

    setMediaItems(mediaItems, mediaItemIndex, C.TIME_UNSET)
    playWhenReady = true
    prepare()
}

fun Player.forcePlayFromBeginning(mediaItems: List<MediaItem>) =
    forcePlayAtIndex(mediaItems, 0)

fun Player.enqueue(mediaItems: List<MediaItem>) {
    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
        forcePlayFromBeginning(mediaItems)
    } else {
        addMediaItems(mediaItemCount, mediaItems)
    }
}

fun Player.addNext(mediaItem: MediaItem) {
    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
        forcePlay(mediaItem)
    } else {
        addMediaItem(currentMediaItemIndex + 1, mediaItem)
    }
}
