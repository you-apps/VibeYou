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
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.repository.PlaylistRepository
import app.suhasdissa.vibeyou.backend.viewmodel.state.AlbumInfoState
import app.suhasdissa.vibeyou.utils.asSong
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    var albumInfoState: AlbumInfoState by mutableStateOf(AlbumInfoState.Loading)
        private set

    var albums = playlistRepository.getPlaylists().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    fun getPlaylistInfo(playlist: Album) {
        viewModelScope.launch {
            albumInfoState = AlbumInfoState.Loading
            albumInfoState = try {
                Log.e("PlaylistViewModel", "Getting info")
                val info = playlistRepository.getPlaylist(playlist.id)
                AlbumInfoState.Success(
                    playlist,
                    info.songs.map { it.asSong }
                )
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                AlbumInfoState.Error
            }
        }
    }

    fun newPlaylistWithSongs(album: Album, songs: List<Song>) {
        viewModelScope.launch {
            playlistRepository.newPlaylistWithSongs(album, songs)
        }
    }

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
