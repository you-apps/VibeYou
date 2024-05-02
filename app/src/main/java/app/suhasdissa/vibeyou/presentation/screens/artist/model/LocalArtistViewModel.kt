package app.suhasdissa.vibeyou.presentation.screens.artist.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.ArtistInfoState
import kotlinx.coroutines.launch

class LocalArtistViewModel(
    private val musicRepository: LocalMusicRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val artist = savedStateHandle.toRoute<Destination.LocalArtist>()
    var artistInfoState: ArtistInfoState by mutableStateOf(ArtistInfoState.Loading)
        private set

    init {
        getArtistInfo(artist.artist)
    }

    private fun getArtistInfo(artist: Artist) {
        viewModelScope.launch {
            artistInfoState = ArtistInfoState.Loading
            artistInfoState = try {
                ArtistInfoState.Success(
                    artist,
                    musicRepository.getArtistInfo(artist.artistsText)
                )
            } catch (e: Exception) {
                Log.e("Artist Info", e.toString())
                ArtistInfoState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                LocalArtistViewModel(
                    application.container.localMusicRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }
}