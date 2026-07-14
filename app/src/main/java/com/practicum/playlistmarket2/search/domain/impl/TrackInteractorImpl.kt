package com.practicum.playlistmarket2.search.domain.impl

import com.practicum.playlistmarket2.search.domain.api.TrackRepository
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import java.io.IOException
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(searchText: String, consumer: TrackInteractor.TrackConsumer) {
        val t = Thread {
                consumer.consume(repository.searchTrack(searchText))
        }
        t.start()
    }
}