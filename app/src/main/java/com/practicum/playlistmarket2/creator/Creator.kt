package com.practicum.playlistmarket2.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlistmarket2.data.network.CheckNetworkImpl
import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient
import com.practicum.playlistmarket2.search.data.impl.HistoryDataSourceImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmarket2.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmarket2.search.domain.api.HistoryDataSource
import com.practicum.playlistmarket2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmarket2.settings.data.impl.ThemeDataSourceImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeInteractor
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository
import com.practicum.playlistmarket2.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmarket2.settings.data.impl.ThemeRepositoryImpl

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(checkConnection = CheckNetworkImpl(context)))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun getHistoryDataSource(context: Context): HistoryDataSource{
        return HistoryDataSourceImpl(context.getSharedPreferences("key_for_history", MODE_PRIVATE)
        )
    }
    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(getHistoryDataSource(context))
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getThemeDataSource(context: Context): ThemeDataSourceImpl{
        return ThemeDataSourceImpl(context.getSharedPreferences("dark_theme", MODE_PRIVATE))
    }
    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(getThemeDataSource(context))
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }



}