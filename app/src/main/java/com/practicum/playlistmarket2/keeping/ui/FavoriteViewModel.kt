package com.practicum.playlistmarket2.keeping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarket2.keeping.domain.api.FavoriteTrackState

class FavoriteViewModel: ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTrackState>()
    fun observeState(): LiveData<FavoriteTrackState> = stateLiveData
init {
    renderState(FavoriteTrackState.Empty)
}
    fun renderState(state: FavoriteTrackState){
        stateLiveData.postValue(state)
    }
}