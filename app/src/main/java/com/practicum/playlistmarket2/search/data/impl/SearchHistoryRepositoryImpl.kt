package com.practicum.playlistmarket2.search.data.impl

import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.domain.api.HistoryDataSource
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val dataSource: HistoryDataSource):
    SearchHistoryRepository {

    override fun getHistory(): List<Track> {
        return dataSource.getHistory()
    }

    override fun addToHistory(track: Track) {
        dataSource.addToHistory(track)
    }

    override fun clearHistory() {
        dataSource.clearHistory()
    }

    override fun isEmpty(): Boolean {
        return dataSource.isEmpty()
    }
}