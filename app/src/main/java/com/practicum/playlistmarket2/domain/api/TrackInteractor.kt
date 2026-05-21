package com.practicum.playlistmarket2.domain.api

import com.practicum.playlistmarket2.domain.models.Track

interface TrackInteractor{
    fun searchTrack(searchText: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundMovies: List<Track>)
    }
}