package com.practicum.playlistmarket2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmarket2.di.dataModule
import com.practicum.playlistmarket2.di.interactorModule
import com.practicum.playlistmarket2.di.repositoryModule
import com.practicum.playlistmarket2.di.viewModelModule
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.component.inject

class App: Application(), KoinComponent{
    private var darkTheme = false
    private val themeInteractor: ThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }


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
}