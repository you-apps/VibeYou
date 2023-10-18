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
import app.suhasdissa.vibeyou.ui.dialogs.SortOrder
import kotlinx.coroutines.launch

class LocalSongViewModel(private val musicRepository: LocalMusicRepository) : ViewModel() {
    var songs by mutableStateOf(listOf<Song>())
    var albums by mutableStateOf(listOf<Album>())
    var artists by mutableStateOf(listOf<Artist>())

    var songsSortOrder = SortOrder.Alphabetic
    var reverseSongs = false

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

    fun updateSongsSortOrder() {
        val sortedSongs = when (songsSortOrder) {
            SortOrder.Alphabetic -> songs.sortedBy { it.title.lowercase() }
            SortOrder.Creation_Date -> songs.sortedBy { it.creationDate }
            SortOrder.Date_Added -> songs.sortedBy { it.dateAdded }
            SortOrder.Artist_Name -> songs.sortedBy { it.artistsText.orEmpty().lowercase() }
        }
        songs = if (reverseSongs) sortedSongs.reversed() else sortedSongs
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
