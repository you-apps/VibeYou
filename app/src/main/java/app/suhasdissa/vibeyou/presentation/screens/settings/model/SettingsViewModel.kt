package app.suhasdissa.vibeyou.presentation.screens.settings.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.repository.PipedMusicRepository
import app.suhasdissa.vibeyou.utils.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val pipedMusicRepository: PipedMusicRepository
) : ViewModel() {
    var instances by mutableStateOf(Pref.pipedInstances)

    suspend fun loadInstances() = runCatching {
        instances = withContext(Dispatchers.IO) {
            pipedMusicRepository.pipedApi.getInstanceList()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                SettingsViewModel(application.container.pipedMusicRepository)
            }
        }
    }
}
