package app.suhasdissa.mellowmusic.backend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.LibreMusicApplication
import app.suhasdissa.mellowmusic.backend.database.entities.Song
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
    var searchText = ""
        private set

    fun getSuggestions(query: String) {
        viewModelScope.launch {
            runCatching {
                suggestions = searchRepository.getSuggestions(query)
            }
        }
    }

    fun setSearchHistory() {
        viewModelScope.launch {
            suggestions = searchRepository.getSearchHistory().map { it.query }
        }
    }

    fun searchPiped(query: String) {
        searchText = query
        viewModelScope.launch {
            state = PipedSearchState.Loading
            searchRepository.saveSearchQuery(query)
            state = try {
                PipedSearchState.Success(
                    searchRepository.getSearchResult(query)
                )
            } catch (e: Exception) {
                PipedSearchState.Error(e.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LibreMusicApplication)
                PipedSearchViewModel(
                    application.container.searchRepository
                )
            }
        }
    }
}