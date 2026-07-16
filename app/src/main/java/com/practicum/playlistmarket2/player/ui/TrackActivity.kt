package com.practicum.playlistmarket2.player.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.ActivityTrackBinding
import com.practicum.playlistmarket2.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private val savedTrack: Track by lazy{
        (intent.getParcelableExtra<Track>(ITEM_TRACK) as? Track)!!
    }
    private val viewModel by viewModel<TrackViewModel> {
        (parametersOf(savedTrack))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeTimer().observe(this){
            binding.playTime.text = it
        }

        viewModel.observePLayerState().observe(this){
            when(it){
                TrackViewModel.MEDIA_STATE_PREPARED, TrackViewModel.MEDIA_STATE_DEFAULT -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }

                TrackViewModel.MEDIA_STATE_PLAY ->{
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_stop_100)
                }

                TrackViewModel.MEDIA_STATE_PAUSE -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }
            }
        }

        viewModel.observeTrack().observe(this){
            savedTrack?.let {
                binding.trackName.text = it.trackName
                binding.artistName.text = it.artistName
                binding.trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)
                binding.collectionName.text = it.collectionName
                binding.releaseDate.text = it.releaseDate?.take(4)
                binding.primaryGenreName.text = it.primaryGenreName
                binding.country.text = it.country

                Glide.with(this)
                    .load(getCoverArtwork(it))
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder_312)
                    .transform(RoundedCorners(dpToPx(8f, this)))
                    .into(binding.trackImage)
            }
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.playControl()
        }

        binding.buttonArrow.setOnClickListener {
            finish()
        }

    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    fun getCoverArtwork(track: Track) = track.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val ITEM_TRACK = "item_track"
    }

}