package com.practicum.playlistmarket2.player.domain

import androidx.annotation.DrawableRes
import com.practicum.playlistmarket2.R

sealed class PlayerState(val isPlayButtonEnabled: Boolean, @DrawableRes val buttonImage: Int, val progress: String) {

    class Default : PlayerState(false, R.drawable.ic_button_play_100, "00:00")

    class Prepared : PlayerState(true, R.drawable.ic_button_play_100, "00:00")

    class Playing(progress: String) : PlayerState(true, R.drawable.ic_button_stop_100, progress)

    class Paused(progress: String) : PlayerState(true, R.drawable.ic_button_play_100, progress)
}