package com.practicum.playlistmarket2.mediateka.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_cross_track",
    primaryKeys = ["id", "trackId"],
    indices = [Index(value = ["trackId"])]
)
class PlaylistCrossTrack (
        val id: Long,
        val trackId: String
    )

