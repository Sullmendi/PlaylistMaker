package com.practicum.playlistmarket2.mediateka.ui.one_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistInteractor
import com.practicum.playlistmarket2.mediateka.ui.playlist.CreatePlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val id: Long,
                            playlistInteractor: PlaylistInteractor
): CreatePlaylistViewModel(playlistInteractor){


    init {
        loadPlaylist()
    }

    fun loadPlaylist(){
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(id)
            playlist?.let {
                playlistLiveData.postValue(it)
                makePlaylistName(it.playlistName)
                makePlaylistDescription(it.playlistDescription)
                playlistImageLiveData.postValue(it.playlistImagePath)
            }
        }
    }

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    override fun isAnythingChange(): Boolean {
        return false
    }

    override fun createPlaylist() {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(id)

            val updatedPlaylist = Playlist(
                id = id,
                playlistName = this@EditPlaylistViewModel.playlistName,
                playlistDescription = this@EditPlaylistViewModel.playlistDescription,
                playlistImagePath = playlistImageLiveData.value,
                trackIds = playlist?.trackIds ?: emptyList(),
                tracksCount = playlist?.tracksCount ?: 0
            )

            playlistInteractor.updatePlaylist(updatedPlaylist)
        }
    }
}