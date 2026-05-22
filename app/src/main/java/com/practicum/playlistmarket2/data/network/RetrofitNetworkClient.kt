package com.practicum.playlistmarket2.data.network

import com.practicum.playlistmarket2.data.NetworkClient
import com.practicum.playlistmarket2.data.dto.Response
import com.practicum.playlistmarket2.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackItunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = trackService.findTrack(dto.searchText).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    companion object{
        const val BASE_URL = "https://itunes.apple.com/"
    }
}