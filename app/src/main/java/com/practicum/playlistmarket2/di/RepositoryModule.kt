package com.practicum.playlistmarket2.di
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackIdDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.TracksDbConverter
import com.practicum.playlistmarket2.mediateka.data.impl.FavoriteTrackRepositoryImpl
import com.practicum.playlistmarket2.mediateka.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackRepository
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistRepository
import com.practicum.playlistmarket2.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmarket2.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.settings.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory <TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository>{
        SearchHistoryRepositoryImpl(get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(get())
    }

    factory { TracksDbConverter() }

    factory { PlaylistTrackIdDbConverter() }
    factory { PlaylistDbConverter(get()) }
    factory { PlaylistTrackDbConverter() }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get(), androidContext())
    }
}
