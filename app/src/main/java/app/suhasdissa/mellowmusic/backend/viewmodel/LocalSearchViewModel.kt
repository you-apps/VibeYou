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
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.models.LocalSearchFilter
import app.suhasdissa.mellowmusic.backend.repository.LocalMusicRepository
import app.suhasdissa.mellowmusic.backend.viewmodel.state.PipedSearchState
import kotlinx.coroutines.launch

class LocalSearchViewModel(private val musicRepository: LocalMusicRepository) : ViewModel() {

    var state: PipedSearchState by mutableStateOf(PipedSearchState.Empty)
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
            state = PipedSearchState.Loading
            musicRepository.saveSearchQuery(search)
            state = try {
                when (searchFilter) {
                    LocalSearchFilter.Songs -> {
                        PipedSearchState.Success.Songs(
                            musicRepository.getSearchResult(
                                search
                            )
                        )
                    }

                    LocalSearchFilter.Albums -> {
                        PipedSearchState.Success.Playlists(
                            musicRepository.getPlaylistResult(
                                search
                            )
                        )
                    }

                    LocalSearchFilter.Artists -> {
                        PipedSearchState.Success.Artists(
                            musicRepository.getArtistResult(
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
