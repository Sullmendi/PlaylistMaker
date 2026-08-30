package com.practicum.playlistmarket2.mediateka.ui.favorite_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.api.FavoriteTrackState
import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackInteractor
import com.practicum.playlistmarket2.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor): ViewModel() {

    var favoriteTrackList = mutableListOf<Track>()
    private var isClickAllowed = true
    private val stateLiveData = MutableLiveData<FavoriteTrackState>()
    fun observeState(): LiveData<FavoriteTrackState> = stateLiveData
    private val intentLiveData = MutableLiveData<Track?>()
    fun observeIntent(): LiveData<Track?> = intentLiveData

init {
    appDatabase()
}
    fun appDatabase(){
        viewModelScope.launch {
            favoriteTrackInteractor.favoriteTracks()
                .collect { tracks -> showResults(tracks) }
        }
    }

    fun openTrack(track: Track) {
        if (isClickAllowed){
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
            intentLiveData.value = track
        }
    }

    fun showResults(tracks: List<Track>){
        favoriteTrackList.clear()
        favoriteTrackList.addAll(tracks)
        if(favoriteTrackList.isEmpty()){
            renderState(FavoriteTrackState.Empty)
        } else{
            renderState(FavoriteTrackState.Content(favoriteTrackList))
        }
    }

    fun cleanTrack() {
        intentLiveData.value = null
    }

    fun renderState(state: FavoriteTrackState){
        stateLiveData.postValue(state)
    }
}