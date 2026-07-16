package com.practicum.playlistmarket2.settings.domain.api

interface ThemeRepository {
    fun saveTheme(isThemeDark: Boolean)
    fun getTheme(): Boolean
}