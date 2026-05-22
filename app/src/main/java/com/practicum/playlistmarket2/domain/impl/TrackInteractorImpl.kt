package com.practicum.playlistmarket2.domain.impl

import com.practicum.playlistmarket2.domain.api.TrackInteractor
import com.practicum.playlistmarket2.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(searchText: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(searchText))
        }
    }
}