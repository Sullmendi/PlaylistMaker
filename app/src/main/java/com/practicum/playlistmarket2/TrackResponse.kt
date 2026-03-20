package com.practicum.playlistmarket2

import com.google.gson.annotations.SerializedName

class TrackResponse (
    @SerializedName("results") val trackResults: List<Track>
)

