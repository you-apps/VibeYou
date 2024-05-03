package app.suhasdissa.vibeyou.presentation.screens.onlinemusic.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.SongDatabaseRepository
import app.suhasdissa.vibeyou.domain.models.primary.Song
import kotlinx.coroutines.launch

class SongOptionsViewModel(private val songDatabaseRepository: SongDatabaseRepository) :
    ViewModel() {
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
                SongOptionsViewModel(
                    application.container.songDatabaseRepository
                )
            }
        }
    }
}