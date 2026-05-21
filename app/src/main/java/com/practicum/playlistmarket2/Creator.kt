package com.practicum.playlistmarket2

import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient
import com.practicum.playlistmarket2.data.network.TrackRepositoryImpl
import com.practicum.playlistmarket2.domain.api.TrackInteractor
import com.practicum.playlistmarket2.domain.api.TrackRepository
import com.practicum.playlistmarket2.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
}