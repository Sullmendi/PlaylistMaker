package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreatePlaylistViewModel (private val playlistInteractor: PlaylistInteractor) : ViewModel() {


    var playlistName: String = ""
    private var playlistDescription: String = ""
    private val playlistImageLiveData = MutableLiveData<String?>(null)
    val playlistImageObserve: LiveData<String?> = playlistImageLiveData

    private val stateEnableButton = MutableLiveData<Boolean>(false)
    fun observeStateEnableButton(): LiveData<Boolean> = stateEnableButton

    fun makePlaylistName(name: String) {
        playlistName = name
        stateEnableButton.value = name.trim().isNotEmpty()
    }

    fun makePlaylistDescription(description: String) {
        playlistDescription = description
    }

    fun isAnythingChange(): Boolean {
        val change: Boolean = playlistName.trim().isNotEmpty() || playlistDescription.trim()
            .isNotEmpty() || playlistImageLiveData.value != null
        return change
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch {
            val imagePath = playlistInteractor.saveImageToPrivateStorage(uri)
            playlistImageLiveData.value = imagePath
        }
    }

    fun createPlaylist() {
        viewModelScope.launch{
            val newPlaylist = Playlist(
                id = 0,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistImagePath = playlistImageLiveData.value,
                trackIds = emptyList(),
                tracksCount = 0
            )
            playlistInteractor.createPlaylist(newPlaylist)
        }
    }
}