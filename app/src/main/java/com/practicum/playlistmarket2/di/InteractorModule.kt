package com.practicum.playlistmarket2.di

import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmarket2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module{

    single<TrackInteractor>{
        TrackInteractorImpl(get())
    }

    single<ThemeInteractor>{
        ThemeInteractorImpl(get())
    }

    single<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }
}