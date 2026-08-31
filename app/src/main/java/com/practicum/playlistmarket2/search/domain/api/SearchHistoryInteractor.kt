package com.practicum.playlistmarket2.search.domain.api

import com.practicum.playlistmarket2.domain.models.Track

interface SearchHistoryInteractor {
    suspend fun getHistory(): List<Track>
    suspend fun addToHistory(track: Track)
    fun clearHistory()
    suspend fun isEmpty(): Boolean
}