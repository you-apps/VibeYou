package app.suhasdissa.libremusic.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import app.suhasdissa.libremusic.LibreMusicApplication
import app.suhasdissa.libremusic.backend.database.entities.Song
import app.suhasdissa.libremusic.backend.repository.SongRepository
import app.suhasdissa.libremusic.utils.asMediaItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val songRepository: SongRepository,
    private val controllerFuture: ListenableFuture<MediaController>
) :
    ViewModel() {
    lateinit var controller: MediaController
        private set

    init {
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
            },
            MoreExecutors.directExecutor()
        )
    }

    fun seekNext() {
        if (controller.hasNextMediaItem())
            controller.seekToNext()
    }

    fun seekTo(ms: Long) {
        controller.seekTo(ms)
    }

    fun playPause() {
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }

    fun seekPrevious() {
        controller.seekToPrevious()
    }

    fun playSong(song: Song) {
        if (controller.isPlaying) {
            enqueueSong(song.asMediaItem)
        } else {
            playSongImmediately(song.asMediaItem)
        }

        viewModelScope.launch {
            songRepository.addSong(song)
        }
    }

    private fun enqueueSong(mediaItem: MediaItem) {
        controller.addMediaItem(mediaItem)
    }

    private fun playSongImmediately(mediaItem: MediaItem) {
        controller.setMediaItem(mediaItem)
        controller.prepare()
        controller.play()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LibreMusicApplication)
                PlayerViewModel(
                    application.container.songRepository,
                    application.container.controllerFuture
                )
            }
        }
    }
}