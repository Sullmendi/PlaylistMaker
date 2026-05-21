package com.practicum.playlistmarket2.data.network

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmarket2.domain.models.Track

class HistoryPreferences (val historySharedPreferences: SharedPreferences){
    val gson = Gson()

    fun readHistory(): List<Track>{
        val json = historySharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        val trackHistory = gson.fromJson(json, Array<Track>::class.java)
        return  trackHistory?.toMutableList() ?: emptyList()
    }

    fun addTrackToHistory(track: Track) {
        val trackHistory = readHistory().toMutableList()
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

    fun cleanHistory(){
        historySharedPreferences.edit { remove(HISTORY_KEY) }
    }

    companion object{
        const val HISTORY_KEY = "key_for_history"
        const val MAX_HISTORY_SIZE = 10
    }
}