package com.practicum.playlistmarket2.data.dto

class PlaylistDto(
        val id: Long,
        val playlistName: String,
        val playlistDescription: String?,
        val playlistImagePath: String?,
        val trackIds: List<String>,
        val tracksCount: Int
)