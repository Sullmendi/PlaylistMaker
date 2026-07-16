package com.practicum.playlistmarket2.settings.domain.api

interface ThemeInteractor {
    fun saveTheme(isThemeDark: Boolean)
    fun getTheme(): Boolean
}