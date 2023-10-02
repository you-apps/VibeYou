package app.suhasdissa.mellowmusic.backend.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.backend.repository.PipedMusicRepository
import app.suhasdissa.mellowmusic.backend.viewmodel.state.PlaylistInfoState
import app.suhasdissa.mellowmusic.utils.asSong
import kotlinx.coroutines.launch

class PlaylistViewModel(private val musicRepository: PipedMusicRepository) : ViewModel() {

    var playlistInfoState: PlaylistInfoState by mutableStateOf(PlaylistInfoState.Loading)
        private set

    fun getPlaylistInfo(playlistId: String) {
        viewModelScope.launch {
            playlistInfoState = PlaylistInfoState.Loading
            playlistInfoState = try {
                val info = musicRepository.getPlaylistInfo(playlistId)
                PlaylistInfoState.Success(
                    info.name,
                    info.thumbnailUrl,
                    info.relatedStreams.map { it.asSong }
                )
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                PlaylistInfoState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                PlaylistViewModel(
                    application.container.pipedMusicRepository
                )
            }
        }
    }
}
