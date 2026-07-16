package com.practicum.playlistmarket2.di
import com.practicum.playlistmarket2.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmarket2.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.settings.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository>{
        SearchHistoryRepositoryImpl(get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(get())
    }
}
