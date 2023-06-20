package app.suhasdissa.mellowmusic.backend.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.LibreMusicApplication
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import app.suhasdissa.mellowmusic.utils.asMediaItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val songRepository: SongRepository,
    private val controllerFuture: ListenableFuture<MediaController>
) :
    ViewModel() {
    var controller: MediaController? by mutableStateOf(null)
        private set

    init {
        controllerFuture.addListener(
            {
                Handler(Looper.getMainLooper()).post {
                    controller = controllerFuture.get()
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    fun seekNext() {
        if (controller!!.hasNextMediaItem())
            controller!!.seekToNext()
    }

    fun seekTo(ms: Long) {
        controller!!.seekTo(ms)
    }

    fun playPause() {
        if (controller!!.isPlaying) {
            controller!!.pause()
        } else {
            controller!!.play()
        }
    }

    fun seekPrevious() {
        controller!!.seekToPrevious()
    }

    fun playSong(song: Song) {
        if (controller!!.isPlaying) {
            enqueueSong(song.asMediaItem)
        } else {
            playSongImmediately(song.asMediaItem)
        }

        viewModelScope.launch {
            songRepository.addSong(song)
        }
    }

    fun shuffleFavourites() {
        viewModelScope.launch {
            val shuffleQueue = songRepository.getFavSongs().shuffled().map { it.asMediaItem }
            shuffleSongs(shuffleQueue)
        }
    }

    fun shuffleAll() {
        viewModelScope.launch {
            val shuffleQueue = songRepository.getAllSongs().shuffled().map { it.asMediaItem }
            shuffleSongs(shuffleQueue)
        }
    }

    private fun shuffleSongs(shuffleQueue: List<MediaItem>) {
        viewModelScope.launch {
            controller!!.stop()
            if (controller!!.mediaItemCount > 1) {
                controller!!.removeMediaItems(0, controller!!.mediaItemCount - 1)
            }
            controller!!.addMediaItems(shuffleQueue)
            if (!controller!!.isPlaying) {
                controller!!.prepare()
                controller!!.play()
            }
        }
    }

    private fun enqueueSong(mediaItem: MediaItem) {
        controller!!.addMediaItem(mediaItem)
    }

    private fun playSongImmediately(mediaItem: MediaItem) {
        controller!!.setMediaItem(mediaItem)
        controller!!.prepare()
        controller!!.play()
    }

    fun toggleFavourite(id: String) {
        viewModelScope.launch {
            val song = songRepository.getSongById(id)
            song?.let {
                songRepository.addSong(it.toggleLike())
            }
        }
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