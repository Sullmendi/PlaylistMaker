package com.practicum.playlistmarket2.ui.activescreens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.ui.activescreens.TrackViewHolder
import com.practicum.playlistmarket2.domain.models.Track

class TrackAdapter (
    private val trackList: List<Track>,
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