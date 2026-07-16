package com.practicum.playlistmarket2.settings.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmarket2.App
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.creator.Creator
import com.practicum.playlistmarket2.search.domain.api.SearchState
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor

class SettingsViewModel(private val themeInteractor: ThemeInteractor) : ViewModel() {

    private val darkThemeLiveData = MutableLiveData<Boolean>()
    fun observeDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    fun setTheme(isThemeDark: Boolean) {
        themeInteractor.saveTheme(isThemeDark)
        darkThemeLiveData.value = isThemeDark
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                val themeInteractor = Creator.provideThemeInteractor(app)

                SettingsViewModel(
                    themeInteractor = themeInteractor
                )
            }
        }
    }
}
