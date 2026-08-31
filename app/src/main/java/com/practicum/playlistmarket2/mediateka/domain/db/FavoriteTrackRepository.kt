package com.practicum.playlistmarket2.mediateka.domain.db

import com.practicum.playlistmarket2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun deleteTrack(track: Track)
    suspend fun insertTrack(track: Track)
}