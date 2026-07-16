package com.practicum.playlistmarket2.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmarket2.settings.domain.api.ThemeDataSource
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository

class ThemeDataSourceImpl(private val sharedPreferences: SharedPreferences): ThemeDataSource {
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