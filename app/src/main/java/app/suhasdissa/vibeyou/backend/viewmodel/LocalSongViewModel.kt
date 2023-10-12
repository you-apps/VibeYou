package app.suhasdissa.vibeyou.backend.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Artist
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import kotlinx.coroutines.launch

class LocalSongViewModel(private val musicRepository: LocalMusicRepository) : ViewModel() {
    var songs by mutableStateOf(listOf<Song>())
    var albums by mutableStateOf(listOf<Album>())
    var artists by mutableStateOf(listOf<Artist>())

    init {
        viewModelScope.launch {
            try {
                songs = musicRepository.getAllSongs()
            } catch (e: Exception) {
                Log.e("Get All Songs", e.message, e)
            }
        }
        viewModelScope.launch {
            try {
                albums = musicRepository.getAllAlbums()
            } catch (e: Exception) {
                Log.e("Get All Albums", e.message, e)
            }
        }
        viewModelScope.launch {
            try {
                artists = musicRepository.getAllArtists()
            } catch (e: Exception) {
                Log.e("Get All Artists", e.message, e)
            }
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
