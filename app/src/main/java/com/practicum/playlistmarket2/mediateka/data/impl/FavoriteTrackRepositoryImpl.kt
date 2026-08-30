package com.practicum.playlistmarket2.mediateka.data.impl

import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackRepository
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.AppDatabase
import com.practicum.playlistmarket2.mediateka.data.converters.TracksDbConverter
import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val tracksDbConverter: TracksDbConverter,
) : FavoriteTrackRepository {

    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val track = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(tracksDbConverter.map(track))
    }

    override suspend fun insertTrack(track: Track){
        appDatabase.trackDao().insertTrack(tracksDbConverter.map(track))
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        val trackDb = tracksDbConverter.map(track)
        val favoriteTrackIds = appDatabase.trackDao().findFavoriteTrack()
        return favoriteTrackIds.contains(trackDb.trackId)
    }

    private fun convertFromTrackEntity(track: List<TracksEntity>): List<Track> {
        return track.map { track -> tracksDbConverter.map(track) }
    }
}