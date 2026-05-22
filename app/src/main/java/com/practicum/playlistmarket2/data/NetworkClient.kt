package com.practicum.playlistmarket2.data

import com.practicum.playlistmarket2.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any) : Response
}