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
import app.suhasdissa.mellowmusic.backend.viewmodel.state.ArtistInfoState
import kotlinx.coroutines.launch

class ArtistViewModel(private val musicRepository: PipedMusicRepository) : ViewModel() {

    var artistInfoState: ArtistInfoState by mutableStateOf(ArtistInfoState.Loading)
        private set

    fun getChannelInfo(channelId: String) {
        viewModelScope.launch {
            artistInfoState = ArtistInfoState.Loading
            artistInfoState = try {
                val info = musicRepository.getChannelInfo(channelId)
                val playlists = musicRepository.getChannelPlaylists(channelId, info.tabs)
                ArtistInfoState.Success(
                    info.name ?: "",
                    info.avatarUrl,
                    info.description,
                    playlists ?: listOf()
                )
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                ArtistInfoState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                ArtistViewModel(application.container.pipedMusicRepository)
            }
        }
    }
}
