package com.practicum.playlistmarket2.mediateka.domain.api

import com.practicum.playlistmarket2.domain.models.Playlist

sealed interface PlaylistState {
    object Empty: PlaylistState
    data class Content(val playlistList: List<Playlist>): PlaylistState
}