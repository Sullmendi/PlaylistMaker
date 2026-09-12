package com.practicum.playlistmarket2.mediateka.data.impl

import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.AppDatabase
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmarket2.mediateka.domain.api.TrackAddedState
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase, private val converter: PlaylistDbConverter, private val trackConverter: PlaylistTrackDbConverter
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(converter.map(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists().map{ entities ->
            entities.map{converter.map(it)}
        }
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ) : TrackAddedState {
        if(playlist.trackIds.contains(track.trackId)){return TrackAddedState.AlreadyAdd(playlist)
        }

        val updateIdInPlaylist = playlist.trackIds.toMutableList().apply { add(track.trackId) }

        val updatedPlaylist = playlist.copy(
            trackIds = updateIdInPlaylist,
            tracksCount = playlist.tracksCount + 1
        )

        appDatabase.playlistDao().updatePlaylist(converter.map(updatedPlaylist))

        appDatabase.playlistTrackDao().insertTrack(trackConverter.map(track))

        return TrackAddedState.Success(updatedPlaylist)
    }

}