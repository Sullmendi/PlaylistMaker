package com.practicum.playlistmarket2.search.domain.impl

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository):
    SearchHistoryInteractor {
    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun isEmpty(): Boolean {
        return repository.isEmpty()
    }

}