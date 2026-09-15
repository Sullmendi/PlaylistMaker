package com.practicum.playlistmarket2.mediateka.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.api.PlaylistState
import com.practicum.playlistmarket2.mediateka.domain.api.TrackAddedState
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import com.practicum.playlistmarket2.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor): ViewModel() {

    var playlistList = mutableListOf<Playlist>()
    private var isClickAllowed = true
    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private val intentLiveData = MutableLiveData<Playlist?>()
    fun observeIntent(): LiveData<Playlist?> = intentLiveData

    init {
        appDatabase()
    }
    fun appDatabase(){
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { playlist -> showResult(playlist) }
        }

    }

    fun showResult(playlist: List<Playlist>){
        playlistList.clear()
        playlistList.addAll(playlist)
        if(playlistList.isEmpty()){
            renderState(PlaylistState.Empty)
        } else{
            renderState(PlaylistState.Content(playlistList))
        }
    }

    fun openPlaylist(playlist: Playlist){
        if (isClickAllowed){
            isClickAllowed = false

            intentLiveData.value = playlist

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
    }

    fun cleanIntent(){
        intentLiveData.value = null
    }

    fun renderState(state: PlaylistState){
        stateLiveData.postValue(state)
    }
}