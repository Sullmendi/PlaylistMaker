package com.practicum.playlistmarket2.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.domain.db.FavoriteTrackInteractor
import com.practicum.playlistmarket2.player.domain.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(private val track: Track, private val mediaPlayer: MediaPlayer, private val favoriteTrackInteractor: FavoriteTrackInteractor): ViewModel() {
    private var timerJob: Job? = null
    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePLayerState(): LiveData<PlayerState> = playerStateLiveData

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    init{
        prepareMediaPlayer()
    }

    private fun prepareMediaPlayer(){
        if (track.previewUrl == null) {
            return
        }
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared())
        }

        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerStateLiveData.postValue(PlayerState.Prepared())
        }
    }

    private fun startPlayMusic(){
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        updateTrackTime()
    }
    fun pausePlayMusic(){
        mediaPlayer.pause()
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun playControl(){
        when(playerStateLiveData.value){
            is PlayerState.Playing -> {
                pausePlayMusic()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayMusic()
            }
            else -> {}
        }
    }

    private fun updateTrackTime() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying){
                delay(TIME_DELAY)
                playerStateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    fun onFavoriteClicked(){
        viewModelScope.launch {
            if(track.isFavorite){
                favoriteTrackInteractor.deleteFavoriteTrack(track)
            } else{
                favoriteTrackInteractor.addFavoriteTrack(track)
            }
            track.isFavorite = !track.isFavorite
            trackLiveData.postValue(track)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        playerStateLiveData.value = PlayerState.Default()
    }

    companion object {
        const val TIME_DELAY = 300L
    }
}