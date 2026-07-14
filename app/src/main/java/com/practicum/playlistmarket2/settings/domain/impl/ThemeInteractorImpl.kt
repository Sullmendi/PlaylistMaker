package com.practicum.playlistmarket2.settings.domain.impl

import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository): ThemeInteractor {
    override fun saveTheme(isThemeDark: Boolean) {
        repository.saveTheme(isThemeDark)
    }

    override fun getTheme() : Boolean {
        return repository.getTheme()
    }

}