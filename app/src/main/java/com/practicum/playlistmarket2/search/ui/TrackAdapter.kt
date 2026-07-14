package com.practicum.playlistmarket2.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.player.ui.TrackViewHolder

class TrackAdapter (
    var trackList: List<Track>,
    private val clickOnTrack: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false)
        val viewHolder = TrackViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            clickOnTrack(trackList[position])
        }



        return viewHolder
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

}