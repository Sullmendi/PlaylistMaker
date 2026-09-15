package com.practicum.playlistmarket2.mediateka.ui.one_playlist

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import com.practicum.playlistmarket2.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmarket2.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ChoosePlaylistViewModel(private val id: Long, private val interactor: PlaylistInteractor): ViewModel() {

    init {
        loadPlaylist()
        loadTracks()
    }
    private var isClickAllowed = true

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    private val tracksLiveData = MutableLiveData<List<Track>>()
    fun observeTracks(): LiveData<List<Track>> = tracksLiveData

    private val editIntentLiveData = MutableLiveData<Playlist?>()
    fun observeEditIntent(): LiveData<Playlist?> = editIntentLiveData
    private val intentLiveData = MutableLiveData<Track?>()
    fun observeIntent(): LiveData<Track?> = intentLiveData

    private val shareLiveData = SingleLiveEvent<String>()
    fun observeShare(): LiveData<String> = shareLiveData

    fun loadPlaylist(){
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(id)
            playlistLiveData.postValue(playlist)
        }

    }

    fun openTrack(track: Track) {
        if (isClickAllowed){
            isClickAllowed = false

            intentLiveData.value = track

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
    }

    fun cleanTrack() {
        intentLiveData.value = null
    }

    fun editPlaylist() {
        if (isClickAllowed){
            isClickAllowed = false

            viewModelScope.launch {
                editIntentLiveData.value = interactor.getPlaylistById(id)
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
    }

    fun cleanEdit() {
        editIntentLiveData.value = null
    }

    fun deleteTrack(track: Track){
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(id)
            interactor.deleteTrackFromPlaylist(track, playlist)
            loadPlaylist()
           loadTracks()
        }

    }

    fun loadTracks(){
        viewModelScope.launch {
            interactor.getTracksForPlaylist(id).collect { tracks ->
                tracksLiveData.postValue(tracks)
            }
        }
    }

    fun deletePlaylist(){
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(id)
            interactor.deletePlaylist(playlist)
        }
    }

    fun makeShareMessage(trackCount: String) {
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(id)

            interactor.getTracksForPlaylist(id).take(1).collect { tracks ->
                val messageBody = StringBuilder().apply {
                    appendLine(playlist.playlistName)
                    if (!playlist.playlistDescription.isNullOrEmpty()) {
                        appendLine(playlist.playlistDescription)
                    }
                    appendLine(trackCount)

                    tracks.forEachIndexed { index, track ->
                        val trackNumber = index + 1
                        val trackTime = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                        appendLine("$trackNumber. ${track.artistName} - ${track.trackName} ($trackTime)")
                    }
                }.toString()

                shareLiveData.postValue(messageBody)
            }
        }
    }
}
