package com.practicum.playlistmarket2.mediateka.domain.api

import com.practicum.playlistmarket2.domain.models.Playlist

sealed interface TrackAddedState {
    data class Success(val playlist: Playlist): TrackAddedState
    data class AlreadyAdd(val playlist: Playlist): TrackAddedState
}