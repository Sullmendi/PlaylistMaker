package com.practicum.playlistmarket2.settings.data.impl


import com.practicum.playlistmarket2.settings.domain.api.ThemeDataSource
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val dataSource: ThemeDataSource): ThemeRepository {

    override fun saveTheme(isThemeDark: Boolean) {
        dataSource.saveTheme(isThemeDark)
    }

    override fun getTheme(): Boolean {
        return dataSource.getTheme()
    }
}