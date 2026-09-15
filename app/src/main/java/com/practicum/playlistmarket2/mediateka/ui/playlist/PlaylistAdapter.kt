package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track

class PlaylistAdapter(
    var playlistList: List<Playlist>,
    private val clickOnPlaylist: (Playlist) -> Unit
): RecyclerView.Adapter<PlaylistViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        val viewHolder = PlaylistViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            clickOnPlaylist(playlistList[position])
        }


        return viewHolder
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlistList[position])
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    fun updateData(newPlaylists: List<Playlist>) {
        this.playlistList = newPlaylists
        notifyDataSetChanged()
    }


}