package com.practicum.playlistmarket2.mediateka.domain.impl

import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackInteractor
import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackRepository
import com.practicum.playlistmarket2.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(private val favoriteTrackRepository: FavoriteTrackRepository): FavoriteTrackInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteTrackRepository.favoriteTracks()
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTrackRepository.deleteTrack(track)
    }

    override suspend fun addFavoriteTrack(track: Track) {
        favoriteTrackRepository.insertTrack(track)
    }
}