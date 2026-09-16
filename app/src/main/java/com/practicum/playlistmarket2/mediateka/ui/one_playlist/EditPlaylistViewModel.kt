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

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData
    private var firstPlaylist: Playlist? = null


    fun loadPlaylist(){
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(id)
            playlist?.let {
                firstPlaylist = it
                playlistLiveData.postValue(it)
                makePlaylistName(it.playlistName)
                makePlaylistDescription(it.playlistDescription)
                playlistImageLiveData.postValue(it.playlistImagePath)
            }
        }
    }

    override fun isAnythingChange(): Boolean {
        val original = firstPlaylist ?: return false
        return playlistName != original.playlistName ||
                playlistDescription != original.playlistDescription ||
                playlistImageLiveData.value != original.playlistImagePath
    }

    override fun createPlaylist() {
        viewModelScope.launch {
            val original = firstPlaylist

            val updatedPlaylist = Playlist(
                id = id,
                playlistName = this@EditPlaylistViewModel.playlistName,
                playlistDescription = this@EditPlaylistViewModel.playlistDescription,
                playlistImagePath = this@EditPlaylistViewModel.playlistImageLiveData.value,
                trackIds = original?.trackIds ?: emptyList(),
                tracksCount = original?.tracksCount ?: 0
            )

            playlistInteractor.updatePlaylist(updatedPlaylist)
            firstPlaylist = updatedPlaylist
            playlistLiveData.postValue(updatedPlaylist)
        }
    }
}