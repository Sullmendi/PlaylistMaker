package com.practicum.playlistmarket2.di

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.ui.favorite_track.FavoriteViewModel
import com.practicum.playlistmarket2.mediateka.ui.one_playlist.ChoosePlaylistViewModel
import com.practicum.playlistmarket2.mediateka.ui.playlist.CreatePlaylistViewModel
import com.practicum.playlistmarket2.mediateka.ui.one_playlist.EditPlaylistViewModel
import com.practicum.playlistmarket2.mediateka.ui.playlist.PlaylistViewModel
import com.practicum.playlistmarket2.player.ui.TrackViewModel
import com.practicum.playlistmarket2.search.ui.SearchViewModel
import com.practicum.playlistmarket2.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{ (track: Track) ->
        TrackViewModel(track = track, get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel{
        SettingsViewModel(get())
    }

    viewModel{
        FavoriteViewModel(get())
    }

    viewModel{
        PlaylistViewModel(get())
    }
    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (id: Long) ->
        ChoosePlaylistViewModel(id, get())
    }

    viewModel { (id:Long) ->
        EditPlaylistViewModel(id, get())
    }
}