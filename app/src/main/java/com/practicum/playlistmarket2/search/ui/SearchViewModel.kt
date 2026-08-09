package com.practicum.playlistmarket2.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.api.SearchState
import com.practicum.playlistmarket2.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY

class SearchViewModel(private val trackInteractor: TrackInteractor, private val searchHistoryInteractor: SearchHistoryInteractor): ViewModel() {
    var savedPersonText: String = ""
    private var lastSearchText: String = ""
    var trackList = mutableListOf<Track>()
    var historyTrackList = mutableListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
        lastSearchText = changedText
        val searchRunnable = Runnable { doSearch(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime
        )
    }

    fun doSearch(query:String){

        if(query.isNotEmpty()){
            renderState(
                SearchState.Loading
            )
        }

        trackInteractor.searchTrack(query,object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?) {
                handler.post {
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

        })
    }

    fun renderState(state: SearchState){
        stateLiveData.postValue(state)
    }

    fun openTrack(track: Track) {
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object{
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}