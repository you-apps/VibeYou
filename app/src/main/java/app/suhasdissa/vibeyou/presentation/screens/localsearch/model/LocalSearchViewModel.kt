package app.suhasdissa.vibeyou.presentation.screens.localsearch.model

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
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.models.LocalSearchFilter
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.SearchState
import kotlinx.coroutines.launch

class LocalSearchViewModel(private val musicRepository: LocalMusicRepository) : ViewModel() {

    var state: SearchState by mutableStateOf(SearchState.Empty)
    var history: List<String> by mutableStateOf(listOf())
    var searchFilter = LocalSearchFilter.Songs
    var search by mutableStateOf("")
    var songSearchSuggestion: List<Song> by mutableStateOf(listOf())

    fun getSuggestions() {
        if (search.length < 3) return
        val insertedTextTemp = search
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == search) {
                    viewModelScope.launch {
                        songSearchSuggestion = musicRepository.getSearchResult(search)
                    }
                }
            },
            500L
        )
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
                    LocalSearchFilter.Songs -> {
                        SearchState.Success.Songs(
                            musicRepository.getSearchResult(
                                search
                            )
                        )
                    }

                    LocalSearchFilter.Albums -> {
                        SearchState.Success.Playlists(
                            musicRepository.getAlbumsResult(
                                search
                            )
                        )
                    }

                    LocalSearchFilter.Artists -> {
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

    fun deleteFromHistory(query: String) {
        musicRepository.deleteQuery(query)

        history = history.minus(query)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MellowMusicApplication)
                LocalSearchViewModel(
                    application.container.localMusicRepository
                )
            }
        }
    }
}

