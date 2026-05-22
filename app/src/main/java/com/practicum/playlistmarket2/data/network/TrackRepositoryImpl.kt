package com.practicum.playlistmarket2.data.network

import com.practicum.playlistmarket2.data.NetworkClient
import com.practicum.playlistmarket2.data.dto.TrackResponse
import com.practicum.playlistmarket2.data.dto.TrackSearchRequest
import com.practicum.playlistmarket2.domain.api.TrackRepository
import com.practicum.playlistmarket2.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.String

class TrackRepositoryImpl (private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(searchText: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(searchText))
        if (response.resultCode == 200) {
            return (response as TrackResponse).trackResults.map {
                Track(it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl) }
        } else {
            return emptyList()
        }
    }
}