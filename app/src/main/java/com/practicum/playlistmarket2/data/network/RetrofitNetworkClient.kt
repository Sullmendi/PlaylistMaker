package com.practicum.playlistmarket2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmarket2.data.NetworkClient
import com.practicum.playlistmarket2.data.dto.Response
import com.practicum.playlistmarket2.search.data.TrackSearchRequest
import com.practicum.playlistmarket2.search.data.TrackItunesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackItunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            if(isConnected()){
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

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
    companion object{
        const val BASE_URL = "https://itunes.apple.com/"
    }
}