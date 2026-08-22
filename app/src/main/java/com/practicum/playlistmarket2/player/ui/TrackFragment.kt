package com.practicum.playlistmarket2.player.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FragmentTrackBinding
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.player.domain.PlayerState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class TrackFragment: Fragment() {
    private var _binding: FragmentTrackBinding? = null

    private val binding get() = _binding!!
    private val savedTrack: Track by lazy{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ITEM_TRACK, Track::class.java)!!
        } else {
            (requireArguments().getParcelable<Track>(ITEM_TRACK))!!
        }
    }
    private val viewModel by viewModel<TrackViewModel> {
        parametersOf(savedTrack)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            viewModel.observePLayerState().observe(viewLifecycleOwner){
                binding.buttonPlay.isEnabled = it.isPlayButtonEnabled
                binding.buttonPlay.setImageResource(it.buttonImage)
                binding.playTime.text = it.progress
            }

            viewModel.observeTrack().observe(viewLifecycleOwner){
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
                        .transform(RoundedCorners(dpToPx(8f, requireContext())))
                        .into(binding.trackImage)
                }
            }

            binding.buttonPlay.setOnClickListener {
                viewModel.playControl()
            }

            binding.buttonArrow.setOnClickListener {
                findNavController().popBackStack(R.id.searchFragment, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val ITEM_TRACK = "item_track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ITEM_TRACK to track)
    }

}
