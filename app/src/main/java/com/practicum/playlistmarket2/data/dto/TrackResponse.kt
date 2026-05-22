package com.practicum.playlistmarket2.data.dto

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmarket2.domain.models.Track

class TrackResponse (
    @SerializedName("results") val trackResults: List<TrackDto>
) : Response()