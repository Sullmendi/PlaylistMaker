package com.practicum.playlistmarket2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmarket2.data.CheckNetworkConnection
import com.practicum.playlistmarket2.data.network.NetworkClient
import com.practicum.playlistmarket2.data.dto.Response
import com.practicum.playlistmarket2.search.data.network.TrackSearchRequest
import com.practicum.playlistmarket2.search.data.network.TrackItunesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val checkConnection: CheckNetworkConnection, private val trackService: TrackItunesApi) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            if(checkConnection.isConnected()){
             try {
                 val resp = trackService.findTrack(dto.searchText).execute()

                 val body = resp.body() ?: Response()

                 return body.apply { resultCode = resp.code() }
             } catch (e: Exception){
                 return Response().apply { resultCode= -2 }
             }
        } else {
            return Response().apply { resultCode= -1  }
        }
        } else{
            return Response().apply { resultCode = 400 }
        }
    }
    companion object{
        const val BASE_URL = "https://itunes.apple.com/"
    }
}