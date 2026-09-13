package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist
import java.io.File

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var id: Long = 0

    private val playlistImage: ImageView = itemView.findViewById<ImageView>(R.id.playlist_image)
    private val playlistName: TextView = itemView.findViewById<TextView>(R.id.playlist_name)
    private val countTracks: TextView = itemView.findViewById<TextView>(R.id.count_tracks)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName
        val newCount = playlist.tracksCount
        countTracks.text = itemView.context.resources.getQuantityString(
            R.plurals.tracks_count,
            newCount,
            newCount
        )
        id = playlist.id

        val imagePath = playlist.playlistImagePath
        if(!imagePath.isNullOrEmpty()){
            val imageFile = File(imagePath)
            Glide.with(itemView)
                .load(imageFile)
                .placeholder(R.drawable.ic_placeholder_312)
                .transform(CenterCrop(),RoundedCorners(dpToPx(8f, itemView.context)))
                .into(playlistImage)
        } else{
            Glide.with(itemView)
                .load(R.drawable.ic_placeholder_312)
                .transform(CenterCrop(), RoundedCorners(dpToPx(8f, itemView.context)))
                .into(playlistImage)
        }


    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }


}