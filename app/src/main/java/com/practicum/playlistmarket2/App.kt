package com.practicum.playlistmarket2

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmarket2.creator.Creator
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl

class App: Application(){
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharePreference: SharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        val themeInteractor: ThemeInteractor = Creator.provideThemeInteractor(sharePreference)

        switchTheme(themeInteractor.getTheme())

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

    companion object{
        const val APP_SETTINGS = "app_setting"
    }
}