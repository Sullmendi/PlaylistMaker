package com.practicum.playlistmarket2.search.domain.api

import com.practicum.playlistmarket2.domain.models.Track

sealed interface SearchState {
    object Loading: SearchState
    object emptyHistory : SearchState
    data class ContentSearch(val trackList: List<Track>) : SearchState
    data class History(val historyTrackList: List<Track>): SearchState
    object Error: SearchState
    object Empty: SearchState
}