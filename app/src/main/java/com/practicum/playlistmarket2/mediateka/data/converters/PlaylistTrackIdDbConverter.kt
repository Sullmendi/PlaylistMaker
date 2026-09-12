package com.practicum.playlistmarket2.mediateka.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistTrackIdDbConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTrackIdsList(trackIds: List<String>?): String {
        return gson.toJson(trackIds ?: emptyList<String>())
    }

    @TypeConverter
    fun toTrackIdsList(data: String?): List<String> {
        if (data.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
}