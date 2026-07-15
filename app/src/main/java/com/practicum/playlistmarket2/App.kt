package com.practicum.playlistmarket2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmarket2.settings.data.impl.ThemeDataSourceImpl
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmarket2.settings.data.impl.ThemeRepositoryImpl

class App: Application(){
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences("dark_theme", MODE_PRIVATE)
        switchTheme(ThemeInteractorImpl(ThemeRepositoryImpl(ThemeDataSourceImpl(sharedPreferences))).getTheme())

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkThemeEnabled(): Boolean = darkTheme
}