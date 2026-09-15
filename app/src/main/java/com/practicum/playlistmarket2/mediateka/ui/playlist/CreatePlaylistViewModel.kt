package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel (protected val playlistInteractor: PlaylistInteractor) : ViewModel() {


    var playlistName: String = ""
    protected var playlistDescription: String = ""
    protected val playlistImageLiveData = MutableLiveData<String?>(null)
    val playlistImageObserve: LiveData<String?> = playlistImageLiveData

    private val stateEnableButton = MutableLiveData<Boolean>(false)
    fun observeStateEnableButton(): LiveData<Boolean> = stateEnableButton

    fun makePlaylistName(name: String) {
        playlistName = name
        stateEnableButton.value = name.trim().isNotEmpty()
    }

    fun makePlaylistDescription(description: String?) {
        if (description != null) {
            playlistDescription = description
        }
    }

    open fun isAnythingChange(): Boolean {
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

    open fun createPlaylist() {
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