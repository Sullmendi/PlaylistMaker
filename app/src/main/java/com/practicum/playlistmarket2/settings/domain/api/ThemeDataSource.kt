package com.practicum.playlistmarket2.settings.domain.api

interface ThemeDataSource {
    fun saveTheme(isThemeDark: Boolean)
    fun getTheme(): Boolean
}
