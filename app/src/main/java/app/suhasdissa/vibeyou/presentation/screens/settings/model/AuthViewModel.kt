package app.suhasdissa.vibeyou.backend.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.models.Login
import app.suhasdissa.vibeyou.backend.repository.AuthRepository
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.preferences
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(context: Context, login: Login) {
        viewModelScope.launch {
            Toast.makeText(context, "Authenticating..", Toast.LENGTH_SHORT).show()
            val token = try {
                authRepository.getAuthToken(login)
            } catch (e: Exception) {
                Log.e("Playlist Info", e.toString())
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                null
            }
            token?.token?.let {
                context.preferences.edit {
                    putString(Pref.authTokenKey, it)
                }
            } ?: return@launch
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                AuthViewModel(
                    application.container.authRepository
                )
            }
        }
    }
}
