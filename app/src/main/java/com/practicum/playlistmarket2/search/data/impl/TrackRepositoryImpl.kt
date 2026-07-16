package com.practicum.playlistmarket2.search.data.impl

import com.practicum.playlistmarket2.data.dto.TrackResponse
import com.practicum.playlistmarket2.data.network.NetworkClient
import com.practicum.playlistmarket2.data.network.RetrofitNetworkClient
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.data.network.TrackSearchRequest
import com.practicum.playlistmarket2.search.domain.api.TrackRepository

class TrackRepositoryImpl (private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(searchText: String): List<Track>? {
        val response = networkClient.doRequest(TrackSearchRequest(searchText))
        if (response.resultCode == 200) {
            return (response as TrackResponse).trackResults.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return null
        }
    }
}