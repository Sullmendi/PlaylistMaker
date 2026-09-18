package com.practicum.playlistmarket2.mediateka.ui.playlist.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track

class BottomSheetAdapter(
    private var playlistList: List<Playlist>,
    private val clickOnPlaylist: (Playlist) -> Unit)
    : RecyclerView.Adapter<BottomSheetViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_view_playlist, parent, false)
        val viewHolder = BottomSheetViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            clickOnPlaylist(playlistList[position])
        }

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: BottomSheetViewHolder,
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