package com.practicum.playlistmarket2.mediateka.data.converters

import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity
import com.practicum.playlistmarket2.domain.models.Track

class TracksDbConverter {
    fun map(track: Track): TracksEntity {
        return TracksEntity(track.trackId,
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

    fun map(track: TracksEntity): Track {
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