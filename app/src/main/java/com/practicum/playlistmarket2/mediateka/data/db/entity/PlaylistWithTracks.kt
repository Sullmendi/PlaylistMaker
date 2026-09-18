package com.practicum.playlistmarket2.mediateka.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistCrossTrack::class)
    )
    val tracks: List<PlaylistTrackEntity>
)