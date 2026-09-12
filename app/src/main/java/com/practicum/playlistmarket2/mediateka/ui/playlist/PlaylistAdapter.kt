package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist

class PlaylistAdapter(private val playlistList: List<Playlist>): RecyclerView.Adapter<PlaylistViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        val viewHolder = PlaylistViewHolder(view)

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

}