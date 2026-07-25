package com.practicum.playlistmarket2.di

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.keeping.ui.FavoriteViewModel
import com.practicum.playlistmarket2.keeping.ui.PlaylistViewModel
import com.practicum.playlistmarket2.player.ui.TrackViewModel
import com.practicum.playlistmarket2.search.ui.SearchViewModel
import com.practicum.playlistmarket2.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{ (track: Track) ->
        TrackViewModel(track = track, get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel{
        SettingsViewModel(get())
    }

    viewModel{
        FavoriteViewModel()
    }

    viewModel{
        PlaylistViewModel()
    }

}