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
import kotlinx.coroutines.launch

class PipedSearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    sealed interface PipedSearchState {
        data class Success(val items: List<Song>) : PipedSearchState
        data class Error(val error: String) : PipedSearchState
        object Loading : PipedSearchState
        object Empty : PipedSearchState
    }

    var state: PipedSearchState by mutableStateOf(PipedSearchState.Empty)
    var suggestions: List<String> by mutableStateOf(listOf())
    var searchFilter = SearchFilter.Songs
    var search by mutableStateOf("")

    fun getSuggestions() {
        if (search.length < 3) return
        val insertedTextTemp = search
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == search) {
                    viewModelScope.launch {
                        runCatching {
                            suggestions = searchRepository.getSuggestions(search)
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
            suggestions = searchRepository.getSearchHistory().reversed().map { it.query }
        }
    }

    fun searchPiped() {
        if (search.isEmpty()) return
        viewModelScope.launch {
            state = PipedSearchState.Loading
            searchRepository.saveSearchQuery(search)
            state = try {
                PipedSearchState.Success(
                    searchRepository.getSearchResult(search, searchFilter)
                )
            } catch (e: Exception) {
                PipedSearchState.Error(e.toString())
            }
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
