package app.suhasdissa.vibeyou.presentation.screens.album.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.PlaylistRepository
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Song
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    fun newPlaylistWithSongs(album: Album, songs: List<Song>) {
        viewModelScope.launch {
            playlistRepository.newPlaylistWithSongs(album, songs)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                NewPlaylistViewModel(
                    application.container.playlistRepository
                )
            }
        }
    }
}