package app.suhasdissa.vibeyou.presentation.screens.album.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.AlbumInfoState
import kotlinx.coroutines.launch

class LocalPlaylistViewModel(
    private val musicRepository: LocalMusicRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val playlist = savedStateHandle.toRoute<Destination.LocalPlaylists>()

    var albumInfoState: AlbumInfoState by mutableStateOf(AlbumInfoState.Loading)
        private set

    init {
        getAlbumInfo(playlist.album)
    }

    private fun getAlbumInfo(album: Album) {
        viewModelScope.launch {
            albumInfoState = AlbumInfoState.Loading
            albumInfoState = try {
                AlbumInfoState.Success(
                    album,
                    musicRepository.getAlbumInfo(album.id.toLong())
                )
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                AlbumInfoState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                LocalPlaylistViewModel(
                    application.container.localMusicRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}