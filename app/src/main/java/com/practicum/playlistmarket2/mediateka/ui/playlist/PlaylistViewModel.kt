package com.practicum.playlistmarket2.mediateka.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarket2.mediateka.domain.api.PlaylistState

class PlaylistViewModel: ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData
    init {
        renderState(PlaylistState.Empty)
    }
    fun renderState(state: PlaylistState){
        stateLiveData.postValue(state)
    }
}