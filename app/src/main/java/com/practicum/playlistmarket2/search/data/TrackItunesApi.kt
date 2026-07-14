package com.practicum.playlistmarket2.search.data

import com.practicum.playlistmarket2.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackItunesApi {

    @GET("search?entity=song")
    fun findTrack(@Query("term") text: String) : Call<TrackResponse>
}