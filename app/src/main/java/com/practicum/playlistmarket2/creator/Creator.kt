package com.practicum.playlistmarket2.creator

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmarket2.search.domain.impl.TrackRepositoryImpl
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.search.domain.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmarket2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmarket2.search.ui.TrackAdapter
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmarket2.settings.domain.impl.ThemeRepositoryImpl

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }

}