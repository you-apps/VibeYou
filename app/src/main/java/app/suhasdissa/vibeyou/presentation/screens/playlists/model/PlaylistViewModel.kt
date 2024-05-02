package app.suhasdissa.vibeyou.presentation.screens.playlists.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.PlaylistRepository
import app.suhasdissa.vibeyou.domain.models.primary.Album
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {


    var albums = playlistRepository.getPlaylists().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    fun deletePlaylist(album: Album) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(album)
        }
    }

    fun clearPlaylist(album: Album) {
        viewModelScope.launch {
            playlistRepository.clearPlaylist(album)
        }
    }

    fun deletePlaylistAndSongs(album: Album) {
        viewModelScope.launch {
            playlistRepository.deletePlaylistAndSongs(album)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                PlaylistViewModel(
                    application.container.playlistRepository
                )
            }
        }
    }
}

