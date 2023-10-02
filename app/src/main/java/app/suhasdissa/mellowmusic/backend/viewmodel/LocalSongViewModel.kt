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
import app.suhasdissa.mellowmusic.backend.repository.LocalMusicRepository
import kotlinx.coroutines.launch

class LocalSongViewModel(private val musicRepository: LocalMusicRepository) : ViewModel() {
    var songs by mutableStateOf(listOf<Song>())

    init {
        viewModelScope.launch {
            songs = musicRepository.getAllSongs()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                LocalSongViewModel(
                    application.container.localMusicRepository
                )
            }
        }
    }
}
