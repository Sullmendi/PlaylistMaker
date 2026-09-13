package com.practicum.playlistmarket2.mediateka.data.converters

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity

class PlaylistTrackDbConverter {
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun map(track: PlaylistTrackEntity): Track {
        return Track(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }
}