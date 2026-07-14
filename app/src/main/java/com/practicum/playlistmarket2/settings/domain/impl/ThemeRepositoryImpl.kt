package com.practicum.playlistmarket2.settings.domain.impl

import android.content.SharedPreferences
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences): ThemeRepository {
    override fun saveTheme(isThemeDark: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME, isThemeDark).apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }
    companion object{
        const val DARK_THEME = "dark_theme"
    }
}
