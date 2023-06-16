package app.suhasdissa.libremusic.backend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.libremusic.LibreMusicApplication
import app.suhasdissa.libremusic.backend.database.entities.Song
import app.suhasdissa.libremusic.backend.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {
    var songs by mutableStateOf<List<Song>?>(null)
        private set

    fun getAllSongs() {
        viewModelScope.launch {
            songs = songRepository.getAllSongs()
        }
    }

    fun getFavouriteSongs() {
        viewModelScope.launch {
            songs = songRepository.getFavSongs()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibreMusicApplication)
                SongViewModel(
                    application.container.songRepository
                )
            }
        }
    }
}