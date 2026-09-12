package com.practicum.playlistmarket2.mediateka.domain.db

import android.net.Uri
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.api.TrackAddedState
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): TrackAddedState

    suspend fun saveImageToPrivateStorage(uri: Uri): String
}