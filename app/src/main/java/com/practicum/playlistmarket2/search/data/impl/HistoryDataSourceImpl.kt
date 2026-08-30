package com.practicum.playlistmarket2.search.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.AppDatabase
import com.practicum.playlistmarket2.search.domain.api.HistoryDataSource
import kotlinx.coroutines.flow.map

class HistoryDataSourceImpl(private val historySharedPreferences: SharedPreferences, private val gson: Gson, val appDatabase: AppDatabase):
    HistoryDataSource {
    override suspend fun getHistory(): List<Track> {
        val json = historySharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        val trackHistory = gson.fromJson(json, Array<Track>::class.java)
        val listFavoriteId = appDatabase.trackDao().findFavoriteTrack()

        for(track in trackHistory){
            if(listFavoriteId.contains(track.trackId)){
                track.isFavorite = true
            }
        }
        return  trackHistory?.toMutableList() ?: emptyList()
    }

    override suspend fun addToHistory(track: Track) {
        val trackHistory = getHistory().toMutableList()
        trackHistory.removeIf { it.trackId == track.trackId }
        trackHistory.add(0,track)
        if (trackHistory.size > MAX_HISTORY_SIZE){
            trackHistory.removeAt(trackHistory.lastIndex)
        }

        val json = gson.toJson(trackHistory)
        historySharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistory() {
        historySharedPreferences.edit { remove(HISTORY_KEY) }
    }


    override suspend fun isEmpty(): Boolean {
        val trackHistory = getHistory().toMutableList()
        if(trackHistory.isEmpty()){
            return true
        } else{
            return false
        }
    }

    companion object{
        const val HISTORY_KEY = "key_for_history"
        const val MAX_HISTORY_SIZE = 10
    }
}