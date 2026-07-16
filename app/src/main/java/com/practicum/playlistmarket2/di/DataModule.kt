package com.practicum.playlistmarket2.di
import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmarket2.data.CheckNetworkConnection
import com.practicum.playlistmarket2.data.network.CheckNetworkImpl
import com.practicum.playlistmarket2.data.network.NetworkClient
import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient
import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient.Companion.BASE_URL
import com.practicum.playlistmarket2.search.data.impl.HistoryDataSourceImpl
import com.practicum.playlistmarket2.search.data.network.TrackItunesApi
import com.practicum.playlistmarket2.search.domain.api.HistoryDataSource
import com.practicum.playlistmarket2.settings.data.impl.ThemeDataSourceImpl
import com.practicum.playlistmarket2.settings.domain.api.ThemeDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<TrackItunesApi> {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrackItunesApi::class.java)
        }

    single(named("history")) {
            androidContext()
                .getSharedPreferences("key_for_history", Context.MODE_PRIVATE)
    }

    single(named("theme")) {
        androidContext()
            .getSharedPreferences("dark_theme", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<HistoryDataSource>{
        HistoryDataSourceImpl(get(named("history")), get())
    }

    single<ThemeDataSource>{
        ThemeDataSourceImpl(get(named("theme")))
    }

    single<CheckNetworkConnection>{
        CheckNetworkImpl(androidContext())
    }

    single<NetworkClient>{
        RetrofitNetworkClient(get(), get())
    }

    }
