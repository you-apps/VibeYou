package app.suhasdissa.vibeyou.presentation.screens.player.model

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.backend.repository.PipedMusicRepository
import app.suhasdissa.vibeyou.backend.repository.SongDatabaseRepository
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.utils.addNext
import app.suhasdissa.vibeyou.utils.asMediaItem
import app.suhasdissa.vibeyou.utils.enqueue
import app.suhasdissa.vibeyou.utils.forcePlay
import app.suhasdissa.vibeyou.utils.forcePlayFromBeginning
import app.suhasdissa.vibeyou.utils.playGracefully
import app.suhasdissa.vibeyou.utils.playPause
import app.suhasdissa.vibeyou.utils.seek
import app.suhasdissa.vibeyou.utils.seekNext
import app.suhasdissa.vibeyou.utils.services.PlayerService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val songDatabaseRepository: SongDatabaseRepository,
    private val musicRepository: PipedMusicRepository,
    private val localMusicRepository: LocalMusicRepository,
    private val controllerFuture: ListenableFuture<MediaController>
) :
    ViewModel() {
    var controller: MediaController? by mutableStateOf(null)
        private set

    private var toBePlayed: MediaItem? = null

    init {
        Log.e("PlayerViewModel", "Initializing")
        controllerFuture.addListener(
            {
                Handler(Looper.getMainLooper()).post {
                    controller = controllerFuture.get()
                    toBePlayed?.let {
                        controller?.forcePlay(it)
                    }
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    fun seekNext() {
        controller!!.seekNext()
    }

    fun seekTo(ms: Long) {
        controller!!.seek(ms)
    }

    fun playPause() {
        controller!!.playPause()
    }

    fun seekPrevious() {
        controller!!.seekToPrevious()
    }

    fun playSongs(songs: List<Song>, shuffle: Boolean = false) {
        viewModelScope.launch {
            val queue = if (shuffle) {
                songs.shuffled()
            } else {
                songs
            }
                .map { it.asMediaItem }
            playAll(queue)
        }
    }

    private fun playAll(newQueue: List<MediaItem>) {
        viewModelScope.launch {
            controller!!.forcePlayFromBeginning(newQueue)
        }
    }

    fun saveSong(song: Song) {
        viewModelScope.launch {
            songDatabaseRepository.addSong(song)
        }
    }

    fun saveSongs(songs: List<Song>) {
        viewModelScope.launch {
            songDatabaseRepository.addSongs(songs)
        }
    }

    fun playNext(song: Song) {
        controller!!.addNext(song.asMediaItem)
    }

    fun enqueueSong(song: Song) {
        controller!!.enqueue(song.asMediaItem)
    }

    fun playSong(song: Song) {
        controller!!.playGracefully(song.asMediaItem)
    }

    fun setPlaybackParams(speed: Float, pitch: Float) {
        controller!!.playbackParameters = PlaybackParameters(speed, pitch)
    }

    fun getPlaybackParams() = controller!!.playbackParameters

    fun toggleFavourite(id: String) {
        viewModelScope.launch {
            val song = songDatabaseRepository.getSongById(id)
            song?.let {
                songDatabaseRepository.addSong(it.toggleLike())
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun updateEqualizerSettings() {
        val command = SessionCommand(PlayerService.COMMAND_UPDATE_EQUALIZER, Bundle.EMPTY)
        controller!!.sendCustomCommand(command, Bundle.EMPTY)
    }

    suspend fun isFavourite(id: String): Boolean {
        val song = songDatabaseRepository.getSongById(id)
        song ?: return false
        return song.isFavourite
    }

    fun tryToPlayId(id: String) {
        viewModelScope.launch {
            val song: Song? = musicRepository.searchSongId(id)
            song?.let {
                if (controller == null) {
                    toBePlayed = song.asMediaItem
                } else {
                    controller?.forcePlay(song.asMediaItem)
                }
            }
        }
    }

    fun tryToPlayUri(uri: Uri) {
        viewModelScope.launch {
            val song: Song? = localMusicRepository.getSongFromUri(uri)
            song?.let {
                if (controller == null) {
                    toBePlayed = song.asMediaItem
                } else {
                    controller?.forcePlay(song.asMediaItem)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MellowMusicApplication)
                PlayerViewModel(
                    application.container.songDatabaseRepository,
                    application.container.pipedMusicRepository,
                    application.container.localMusicRepository,
                    application.container.controllerFuture
                )
            }
        }
    }
}
