package com.practicum.playlistmarket2.di

import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackInteractor
import com.practicum.playlistmarket2.mediateka.domain.impl.FavoriteTrackInteractorImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmarket2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module{

    factory <TrackInteractor>{
        TrackInteractorImpl(get())
    }

    factory <ThemeInteractor>{
        ThemeInteractorImpl(get())
    }

    factory <SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }

    single<FavoriteTrackInteractor>{
        FavoriteTrackInteractorImpl(get())
    }
}