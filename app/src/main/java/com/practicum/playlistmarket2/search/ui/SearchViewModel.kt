package com.practicum.playlistmarket2.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.SearchState
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val trackInteractor: TrackInteractor, private val searchHistoryInteractor: SearchHistoryInteractor): ViewModel() {
    var savedPersonText: String = ""
    private var lastSearchText: String = ""
    var trackList = mutableListOf<Track>()
    var historyTrackList = mutableListOf<Track>()
    private var isClickAllowed = true
    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val intentLiveData = MutableLiveData<Track?>()
    fun observeIntent(): LiveData<Track?> = intentLiveData

    init {
        val searchHistory = searchHistoryInteractor.getHistory()
        if(searchHistory.isNotEmpty()){
            stateLiveData.value = SearchState.History(searchHistory)
        } else{
            stateLiveData.value = SearchState.emptyHistory
        }
    }


    fun searchDebounce(changedText: String){
        if(savedPersonText == changedText){
            return
        }
        this.savedPersonText = changedText
        lastSearchText = changedText

        if(changedText.isEmpty()) {
            if (searchHistoryInteractor.isEmpty()) {
                renderState(
                    SearchState.emptyHistory
                )
            } else {
                renderState(
                    SearchState.History(searchHistoryInteractor.getHistory())
                )
            }
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            doSearch(changedText)
        }
    }

    fun doSearch(query:String){

        if(query.isNotEmpty()){
            renderState(
                SearchState.Loading
            )
        }

        viewModelScope.launch {
            trackInteractor
                .searchTrack(query)
                .collect { pair ->
                    val foundTracks = pair.first
                    val tracks = mutableListOf<Track>()
                    if (foundTracks!= null){
                        tracks.addAll(foundTracks)
                    }
                    when {
                        foundTracks == null -> {
                            renderState(
                                SearchState.Error
                            )
                        }
                        tracks.isEmpty() -> {
                            renderState(
                                SearchState.Empty
                            )
                        }
                        else ->{
                            renderState(
                                SearchState.ContentSearch(tracks)
                            )
                        }
                    }
                }
        }
    }

    fun renderState(state: SearchState){
        stateLiveData.postValue(state)
    }

    fun openTrack(track: Track) {
        if (isClickAllowed){
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
            searchHistoryInteractor.addToHistory(track)
            intentLiveData.value = track
        }
    }

    fun cleanTrack() {
        intentLiveData.value = null
    }

    fun showHistory(){
        val sherPref = searchHistoryInteractor.getHistory()
        if (sherPref.isNotEmpty()) {
            renderState(
                SearchState.History(sherPref)
            )
        } else{
           renderState(
               SearchState.emptyHistory
           )
        }
    }

    fun clearHistory(){
        searchHistoryInteractor.clearHistory()
        renderState(
            SearchState.emptyHistory
        )
    }
    fun readHistory(): List<Track>{
        return searchHistoryInteractor.getHistory()
    }

    fun lastSearchRetry(){
        if(lastSearchText.isNotEmpty()){
        doSearch(lastSearchText)
        } else{
            showHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object{
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}