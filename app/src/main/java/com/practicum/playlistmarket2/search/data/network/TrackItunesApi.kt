package com.practicum.playlistmarket2.search.data.network

import com.practicum.playlistmarket2.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackItunesApi {

    @GET("search?entity=song")
    suspend fun findTrack(@Query("term") text: String) : TrackResponse
}