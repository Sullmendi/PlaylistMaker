package com.practicum.playlistmarket2.search.domain.api

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(searchText: String): Flow<Resource<List<Track>>>
}