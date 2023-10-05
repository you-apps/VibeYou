package app.suhasdissa.mellowmusic.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.repository.SongDatabaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(private val songDatabaseRepository: SongDatabaseRepository) : ViewModel() {
    val songs = songDatabaseRepository.getAllSongsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    val favSongs = songDatabaseRepository.getFavSongsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    fun removeSong(song: Song) {
        viewModelScope.launch {
            songDatabaseRepository.removeSong(song)
        }
    }

    fun toggleFavourite(id: String) {
        viewModelScope.launch {
            val song = songDatabaseRepository.getSongById(id)
            song ?: return@launch
            song.let {
                songDatabaseRepository.addSong(it.toggleLike())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                SongViewModel(
                    application.container.songDatabaseRepository
                )
            }
        }
    }
}
