package com.practicum.playlistmarket2.domain.models

data class Playlist(
    val id: Long = 0,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistImagePath: String?,
    val trackIds: List<String>,
    val tracksCount: Int
)