package com.practicum.playlistmarket2.search.domain.api

import com.practicum.playlistmarket2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor{
    fun searchTrack(searchText: String): Flow<Pair<List<Track>?, String?>>
}