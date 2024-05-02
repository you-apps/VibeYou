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
import app.suhasdissa.vibeyou.backend.repository.PipedMusicRepository
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.AlbumInfoState
import app.suhasdissa.vibeyou.utils.asSong
import kotlinx.coroutines.launch

class OnlinePlaylistViewModel(
    private val musicRepository: PipedMusicRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val playlist = savedStateHandle.toRoute<Destination.Playlists>()

    var albumInfoState: AlbumInfoState by mutableStateOf(AlbumInfoState.Loading)
        private set

    init {
        getPlaylistInfo(playlist.album)
    }

    private fun getPlaylistInfo(playlist: Album) {
        viewModelScope.launch {
            albumInfoState = AlbumInfoState.Loading
            albumInfoState = try {
                val info = musicRepository.getPlaylistInfo(playlist.id)
                AlbumInfoState.Success(
                    playlist,
                    info.relatedStreams.map { it.asSong }
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
                OnlinePlaylistViewModel(
                    application.container.pipedMusicRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}