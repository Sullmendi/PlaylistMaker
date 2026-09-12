package com.practicum.playlistmarket2.mediateka.domain.impl

import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.api.TrackAddedState
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ): TrackAddedState {
        return repository.addTrackToPlaylist(track,playlist)
    }
}