package com.practicum.playlistmarket2.mediateka.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistImagePath: String?,
    val trackIdsJson: String = "[]",
    val tracksCount: Int = 0
)