package com.practicum.playlistmarket2.search.data.impl

import com.practicum.playlistmarket2.data.dto.TrackResponse
import com.practicum.playlistmarket2.data.network.NetworkClient
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.AppDatabase
import com.practicum.playlistmarket2.search.data.network.TrackSearchRequest
import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl (private val networkClient: NetworkClient, private val appDatabase: AppDatabase) : TrackRepository {

    override fun searchTrack(searchText: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(searchText))
        if (response.resultCode == 200) {
            with (response as TrackResponse) {
                val data = trackResults.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
                val listFavoriteTracks = appDatabase.trackDao().findFavoriteTrack()
                for (track in data ){
                    if (listFavoriteTracks.contains(track.trackId)){
                        track.isFavorite = true
                    }
                }
                emit(Resource.Success(data))
            }
        } else
            emit(Resource.Error())
        }
    }