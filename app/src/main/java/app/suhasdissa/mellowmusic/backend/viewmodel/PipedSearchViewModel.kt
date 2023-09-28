package app.suhasdissa.mellowmusic.backend.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.repository.SearchRepository
import app.suhasdissa.mellowmusic.backend.viewmodel.state.PipedSearchState
import kotlinx.coroutines.launch

class PipedSearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    var state: PipedSearchState by mutableStateOf(PipedSearchState.Empty)
    var suggestions: List<String> by mutableStateOf(listOf())
    var history: List<String> by mutableStateOf(listOf())
    var searchFilter = SearchFilter.Songs
    var search by mutableStateOf("")
    var songSearchSuggestion: List<Song> by mutableStateOf(listOf())

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
                            suggestions = searchRepository.getSuggestions(search).take(6)
                        }
                        Log.e("Search ViewModel", "getting query for \"$search\"")
                    }
                }
            },
            500L
        )
    }

    fun setSearchHistory() {
        viewModelScope.launch {
            history = searchRepository.getSearchHistory().takeLast(6).reversed().map { it.query }
        }
    }

    fun searchPiped() {
        if (search.isEmpty()) return
        viewModelScope.launch {
            state = PipedSearchState.Loading
            searchRepository.saveSearchQuery(search)
            state = try {
                when (searchFilter) {
                    SearchFilter.Songs, SearchFilter.Videos -> {
                        PipedSearchState.Success.Songs(
                            searchRepository.getSearchResult(
                                search,
                                searchFilter
                            )
                        )
                    }

                    SearchFilter.Albums, SearchFilter.Playlists -> {
                        PipedSearchState.Success.Playlists(
                            searchRepository.getPlaylistResult(
                                search,
                                searchFilter
                            )
                        )
                    }

                    SearchFilter.Artists -> {
                        PipedSearchState.Success.Artists(
                            searchRepository.getArtistResult(
                                search
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Search Piped", e.toString())
                PipedSearchState.Error(e.toString())
            }
        }
    }

    private fun searchSongs(search: String) {
        val searchQuery = search.split(" ").joinToString("%")
        viewModelScope.launch {
            songSearchSuggestion = searchRepository.searchLocalSong("%$searchQuery%")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MellowMusicApplication)
                PipedSearchViewModel(
                    application.container.searchRepository
                )
            }
        }
    }
}
