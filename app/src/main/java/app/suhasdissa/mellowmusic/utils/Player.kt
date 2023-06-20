package app.suhasdissa.mellowmusic.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Timeline

inline val Timeline.windows: List<Timeline.Window>
    get() = List(windowCount) {
        getWindow(it, Timeline.Window())
    }

inline val Timeline.queue: List<Pair<Int,MediaItem>>
    get() = List(windowCount) {
        it to getWindow(it, Timeline.Window()).mediaItem
    }