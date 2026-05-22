package com.practicum.playlistmarket2.domain.api

import com.practicum.playlistmarket2.domain.models.Track

interface TrackRepository {
    fun searchTrack(searchText: String): List<Track>
}