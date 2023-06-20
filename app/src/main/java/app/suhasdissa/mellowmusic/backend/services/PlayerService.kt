package app.suhasdissa.mellowmusic.backend.services

import android.content.Intent
import android.media.audiofx.LoudnessEnhancer
import android.os.Handler
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.AudioRendererEventListener
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor
import androidx.media3.exoplayer.audio.SonicAudioProcessor
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.runBlocking

class PlayerService : MediaSessionService(), MediaSession.Callback {
    private var mediaSession: MediaSession? = null
    private lateinit var cache: SimpleCache
    private lateinit var player: ExoPlayer

    private var loudnessEnhancer: LoudnessEnhancer? = null

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val cacheEvictor = NoOpCacheEvictor()
        val directory = cacheDir.resolve("exoplayer").also { directory ->
            directory.mkdir()
        }
        cache = SimpleCache(directory, cacheEvictor, StandaloneDatabaseProvider(this))

        player = ExoPlayer.Builder(this, createRendersFactory(), createMediaSourceFactory())
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .build()

        player.repeatMode = Player.REPEAT_MODE_OFF
        player.playWhenReady = true

        mediaSession = MediaSession.Builder(this, player).setCallback(this).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onDestroy() {
        mediaSession?.run {
            player.stop()
            player.release()
            mediaSession = null
        }
        cache.release()

        loudnessEnhancer?.release()

        super.onDestroy()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun createCacheDataSource(): DataSource.Factory {
        return CacheDataSource.Factory().setCache(cache).apply {
            setUpstreamDataSourceFactory(
                DefaultHttpDataSource.Factory()
                    .setConnectTimeoutMs(16000)
                    .setReadTimeoutMs(8000)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0")
            )
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun createDataSourceFactory(): DataSource.Factory {
        val chunkLength = 512 * 1024L

        return ResolvingDataSource.Factory(createCacheDataSource()) { dataSpec ->
            val videoId = dataSpec.key ?: error("A key must be set")

            if (cache.isCached(videoId, dataSpec.position, chunkLength)) {
                dataSpec
            } else {
                val url = runBlocking {
                    PipedApi.retrofitService.getStreams(videoId).audioStreams[1].url
                }
                url?.let {
                    dataSpec.withUri(url.toUri()).subrange(dataSpec.uriPositionOffset, chunkLength)
                } ?: error("Stream not found")
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun createMediaSourceFactory(): MediaSource.Factory {
        return DefaultMediaSourceFactory(createDataSourceFactory())
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems =
            mediaItems.map {
                it.buildUpon().setUri(it.mediaId).setCustomCacheKey(it.mediaId).build()
            }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun createRendersFactory(): RenderersFactory {
        val audioSink = DefaultAudioSink.Builder()
            .setEnableFloatOutput(false)
            .setEnableAudioTrackPlaybackParams(false)
            .setOffloadMode(DefaultAudioSink.OFFLOAD_MODE_DISABLED)
            .setAudioProcessorChain(
                DefaultAudioSink.DefaultAudioProcessorChain(
                    emptyArray(),
                    SilenceSkippingAudioProcessor(2_000_000, 20_000, 256),
                    SonicAudioProcessor()
                )
            )
            .build()
        return RenderersFactory { handler: Handler?, _, audioListener: AudioRendererEventListener?, _, _ ->
            arrayOf(
                MediaCodecAudioRenderer(
                    this,
                    MediaCodecSelector.DEFAULT,
                    handler,
                    audioListener,
                    audioSink
                )
            )
        }
    }
}