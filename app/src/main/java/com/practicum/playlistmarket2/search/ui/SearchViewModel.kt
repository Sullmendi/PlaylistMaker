package com.practicum.playlistmarket2.search.ui

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmarket2.App
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.creator.Creator
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.player.ui.TrackActivity
import com.practicum.playlistmarket2.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarket2.search.domain.api.TrackInteractor
import com.practicum.playlistmarket2.search.domain.api.SearchState
import com.practicum.playlistmarket2.search.ui.SearchActivity.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmarket2.search.ui.SearchActivity.Companion.ITEM_TRACK

class SearchViewModel(private val context: Context, private val trackInteractor: TrackInteractor, private val searchHistoryInteractor: SearchHistoryInteractor): ViewModel() {
    companion object{
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        const val HISTORY_KEY = "key_for_history"

        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                val sharePref = app.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
                val trackInteractor = Creator.provideTrackInteractor(context)
                val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(sharePref)

                SearchViewModel(
                    context = context,
                    trackInteractor = trackInteractor,
                    searchHistoryInteractor = searchHistoryInteractor
                )
            }
        }
    }

    var savedPersonText: String = ""
    var lastSearchText: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

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
                                SearchState.Error(context.getString(R.string.search_problem))
                            )
                        }
                        tracks.isEmpty() -> {
                            renderState(
                                SearchState.Empty(context.getString(R.string.not_found))
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
            val trackIntent = Intent(context, TrackActivity::class.java).apply {
                putExtra(ITEM_TRACK, track)
            }
            context.startActivity(trackIntent)
        }
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


}