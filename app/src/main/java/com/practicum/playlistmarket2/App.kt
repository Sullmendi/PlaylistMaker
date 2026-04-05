package com.practicum.playlistmarket2

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application(){
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharePreference: SharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        darkTheme = sharePreference.getBoolean(DARK_THEME,false)

        switchTheme(darkTheme)

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
        val sharePreference: SharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        sharePreference.edit()
            .putBoolean(DARK_THEME, darkThemeEnabled)
            .apply()
    }

    companion object{
        const val APP_SETTINGS = "app_setting"
        const val DARK_THEME = "dark_theme"
    }
}