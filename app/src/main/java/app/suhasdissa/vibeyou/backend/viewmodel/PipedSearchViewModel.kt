package app.suhasdissa.vibeyou.backend.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Artist
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.models.SearchFilter
import app.suhasdissa.vibeyou.backend.repository.PipedMusicRepository
import app.suhasdissa.vibeyou.backend.viewmodel.state.AlbumInfoState
import app.suhasdissa.vibeyou.backend.viewmodel.state.ArtistInfoState
import app.suhasdissa.vibeyou.backend.viewmodel.state.SearchState
import app.suhasdissa.vibeyou.utils.asSong
import kotlinx.coroutines.launch

class PipedSearchViewModel(private val musicRepository: PipedMusicRepository) : ViewModel() {

    var state: SearchState by mutableStateOf(SearchState.Empty)
    var suggestions: List<String> by mutableStateOf(listOf())
    var history: List<String> by mutableStateOf(listOf())
    var searchFilter = SearchFilter.Songs
    var search by mutableStateOf("")
    var songSearchSuggestion: List<Song> by mutableStateOf(listOf())
    var albumInfoState: AlbumInfoState by mutableStateOf(AlbumInfoState.Loading)
        private set
    var artistInfoState: ArtistInfoState by mutableStateOf(ArtistInfoState.Loading)
        private set

    fun getSuggestions() {
        if (search.length < 3) return
        searchSongs(search)
        val insertedTextTemp = search
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == search) {
                    viewModelScope.launch {
                        runCatching {
                            suggestions = musicRepository.getSuggestions(search).take(6)
                        }
                        Log.e("Search ViewModel", "getting query for \"$search\"")
                    }
                }
            },
            500L
        )
    }

    fun getPlaylistInfo(playlist: Album) {
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

    fun getChannelInfo(artist: Artist) {
        viewModelScope.launch {
            artistInfoState = ArtistInfoState.Loading
            artistInfoState = try {
                val info = musicRepository.getChannelInfo(artist.id)
                val playlists = musicRepository.getChannelPlaylists(artist.id, info.tabs)
                ArtistInfoState.Success(
                    artist.copy(
                        thumbnailUri = info.avatarUrl?.toUri(),
                        description = info.description
                    ),
                    playlists
                )
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                ArtistInfoState.Error
            }
        }
    }

    fun setSearchHistory() {
        viewModelScope.launch {
            history = musicRepository.getSearchHistory().takeLast(6).reversed().map { it.query }
        }
    }

    fun searchPiped() {
        if (search.isEmpty()) return
        viewModelScope.launch {
            state = SearchState.Loading
            musicRepository.saveSearchQuery(search)
            state = try {
                when (searchFilter) {
                    SearchFilter.Songs, SearchFilter.Videos -> {
                        SearchState.Success.Songs(
                            musicRepository.getSearchResult(
                                search,
                                searchFilter
                            )
                        )
                    }

                    SearchFilter.Albums, SearchFilter.Playlists -> {
                        SearchState.Success.Playlists(
                            musicRepository.getPlaylistResult(
                                search,
                                searchFilter
                            )
                        )
                    }

                    SearchFilter.Artists -> {
                        SearchState.Success.Artists(
                            musicRepository.getArtistResult(
                                search
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Search Piped", e.toString())
                SearchState.Error(e.toString())
            }
        }
    }

    private fun searchSongs(search: String) {
        val searchQuery = search.split(" ").joinToString("%")
        viewModelScope.launch {
            songSearchSuggestion = musicRepository.searchLocalSong("%$searchQuery%")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MellowMusicApplication)
                PipedSearchViewModel(
                    application.container.pipedMusicRepository
                )
            }
        }
    }
}
