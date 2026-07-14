package com.practicum.playlistmarket2.search.domain.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.data.HistoryDataSource
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(val context: Context): SearchHistoryRepository {
    val dataSource = HistoryDataSource(context.getSharedPreferences(HISTORY_KEY, MODE_PRIVATE))

    companion object {
        const val HISTORY_KEY = "key_for_history"
    }

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