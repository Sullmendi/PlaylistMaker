package com.practicum.playlistmarket2.player.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmarket2.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(private val track: Track): ViewModel() {
    private val playerStateLiveData = MutableLiveData(MEDIA_STATE_DEFAULT)
    fun observePLayerState(): LiveData<Int> = playerStateLiveData

    private val timerLiveData = MutableLiveData("00:00")
    fun observeTimer(): LiveData<String> = timerLiveData

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val mediaPlayer = MediaPlayer()
    private val mainHandler = Handler(Looper.getMainLooper())

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
            playerStateLiveData.postValue(MEDIA_STATE_PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(MEDIA_STATE_PREPARED)
            mainHandler.removeCallbacks(updateTrackTime)
        }
    }

    private fun startPlayMusic(){
        mediaPlayer.start()
        playerStateLiveData.postValue(MEDIA_STATE_PLAY)
        mainHandler.post(updateTrackTime)
    }
    fun pausePlayMusic(){
        mediaPlayer.pause()
        playerStateLiveData.postValue(MEDIA_STATE_PAUSE)
        mainHandler.removeCallbacks(updateTrackTime)
    }

    fun playControl(){
        when(playerStateLiveData.value){
            MEDIA_STATE_PLAY -> {
                pausePlayMusic()
            }
            MEDIA_STATE_PREPARED,MEDIA_STATE_PAUSE -> {
                startPlayMusic()
            }
        }
    }

    private val updateTrackTime = object : Runnable {
        override fun run() {
            if(playerStateLiveData.value == MEDIA_STATE_PLAY){
                timerLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
                mainHandler.postDelayed(this,TIME_DELAY)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainHandler.removeCallbacks(updateTrackTime)
        mediaPlayer.release()
    }

    companion object {
        const val MEDIA_STATE_DEFAULT = 0
        const val MEDIA_STATE_PREPARED = 1
        const val MEDIA_STATE_PLAY = 2
        const val MEDIA_STATE_PAUSE = 3
        const val TIME_DELAY = 300L
    }
}