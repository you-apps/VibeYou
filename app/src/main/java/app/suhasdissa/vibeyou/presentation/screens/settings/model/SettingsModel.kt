package app.suhasdissa.vibeyou.presentation.screens.settings.model

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.catpucchinLatte
import app.suhasdissa.vibeyou.utils.mutableStatePreferenceOf
import app.suhasdissa.vibeyou.utils.preferences

class SettingsModel(preferences: SharedPreferences) : ViewModel() {
    enum class Theme(@StringRes val resId: Int) {
        SYSTEM(R.string.system), LIGHT(R.string.light), DARK(R.string.dark),
        AMOLED(R.string.amoled)
    }

    enum class ColorTheme(@StringRes val resId: Int) {
        SYSTEM(R.string.system),
        CATPPUCCIN(R.string.catppuccin)
    }

    private val themeModePref =
        preferences.getString(Pref.themeKey, Theme.SYSTEM.name) ?: Theme.SYSTEM.name

    var themeMode: Theme by mutableStatePreferenceOf(
        Theme.valueOf(themeModePref.uppercase())
    ) {
        preferences.edit { putString(Pref.themeKey, it.name) }
    }

    private val colorThemePref =
        preferences.getString(Pref.colorThemeKey, ColorTheme.SYSTEM.name)
            ?: ColorTheme.SYSTEM.name

    var colorTheme: ColorTheme by mutableStatePreferenceOf(
        ColorTheme.valueOf(colorThemePref.uppercase())
    ) {
        preferences.edit { putString(Pref.colorThemeKey, it.name) }
    }

    var customColor by mutableStatePreferenceOf(
        preferences.getInt(
            Pref.customColorKey,
            catpucchinLatte.first()
        )
    ) {
        preferences.edit { putInt(Pref.customColorKey, it) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val context = this[APPLICATION_KEY] as Context
                SettingsModel(preferences = context.preferences)
            }
        }
    }
}