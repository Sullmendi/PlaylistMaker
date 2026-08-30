package com.practicum.playlistmarket2.mediateka.domain.db

import com.practicum.playlistmarket2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun deleteFavoriteTrack(track: Track)
    suspend fun addFavoriteTrack(track: Track)
    suspend fun isTrackFavorite(track: Track): Boolean
}