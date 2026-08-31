package com.practicum.playlistmarket2.mediateka.domain.api

import com.practicum.playlistmarket2.domain.models.Track

sealed interface FavoriteTrackState {
    object Empty: FavoriteTrackState

    data class Content(val trackList: List<Track>): FavoriteTrackState
}