package com.practicum.playlistmarket2.mediateka.data.converters

import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistEntity

class PlaylistDbConverter(private val converter: PlaylistTrackIdDbConverter) {
    fun map(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(
            playlist.id,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImagePath,
            trackIdsJson = converter.fromTrackIdsList(playlist.trackIds),
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist{
        return Playlist(
            id = playlist.id,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistImagePath = playlist.playlistImagePath,
            trackIds = converter.toTrackIdsList(playlist.trackIdsJson),
            tracksCount = playlist.tracksCount
        )
    }
}

