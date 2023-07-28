package app.suhasdissa.mellowmusic.backend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SongViewModel(private val songRepository: SongRepository) : ViewModel() {
    var songs = songRepository.getAllSongsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )
        private set

    var favSongs = songRepository.getFavSongsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )
        private set

    var recentSongs by mutableStateOf<List<Song>>(listOf())
        private set

    fun getRecentSongs() {
        viewModelScope.launch {
            recentSongs = songRepository.getRecentSongs(6)
        }
    }

    fun removeSong(song: Song) {
        viewModelScope.launch {
            songRepository.removeSong(song)
        }
    }

    fun toggleFavourite(id: String) {
        viewModelScope.launch {
            val song = songRepository.getSongById(id)
            song ?: return@launch
            song.let {
                songRepository.addSong(it.toggleLike())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                SongViewModel(
                    application.container.songRepository
                )
            }
        }
    }
}
