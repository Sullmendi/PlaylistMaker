package com.practicum.playlistmarket2.data.network

import com.practicum.playlistmarket2.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any) : Response
}