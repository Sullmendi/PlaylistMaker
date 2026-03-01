package com.practicum.playlistmarket2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById<ImageView>(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName: TextView = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById<TextView>(R.id.trackTime)

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackTime.text = model.trackTime
        artistName.text = model.artistName
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_45)
            .transform(RoundedCorners(10))
            .into(trackImage)

    }
}

