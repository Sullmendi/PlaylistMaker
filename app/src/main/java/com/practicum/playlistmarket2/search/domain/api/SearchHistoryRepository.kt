package com.practicum.playlistmarket2.search.domain.api

import com.practicum.playlistmarket2.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
    fun isEmpty(): Boolean
}