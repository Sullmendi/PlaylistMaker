package com.practicum.playlistmarket2.settings.domain.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlistmarket2.settings.data.ThemeDataSource
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository

class ThemeRepositoryImpl(val context: Context): ThemeRepository {
    val dataSource = ThemeDataSource(context.getSharedPreferences(DARK_THEME, MODE_PRIVATE))
    override fun saveTheme(isThemeDark: Boolean) {
        dataSource.saveTheme(isThemeDark)
    }

    override fun getTheme(): Boolean {
        return dataSource.getTheme()
    }
    companion object{
        const val DARK_THEME = "dark_theme"
    }
}
