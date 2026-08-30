package com.practicum.playlistmarket2.mediateka.ui.favorite_track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FavoriteTracksFragmentBinding
import com.practicum.playlistmarket2.mediateka.domain.api.FavoriteTrackState
import com.practicum.playlistmarket2.mediateka.ui.favorite_track.FavoriteViewModel
import com.practicum.playlistmarket2.player.ui.TrackFragment
import com.practicum.playlistmarket2.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment: Fragment() {
    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appDatabase()
        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
        viewModel.observeIntent().observe(viewLifecycleOwner){ track ->
            if(track != null){
                findNavController().navigate(R.id.action_mediaFragment_to_trackFragment,
                    TrackFragment.createArgs(track))
                viewModel.cleanTrack()
            }
        }

        binding.recyclerViewFavoriteTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(viewModel.favoriteTrackList) { track ->
            viewModel.openTrack(track)
        }
        binding.recyclerViewFavoriteTrack.adapter = trackAdapter

    }
    fun render(state: FavoriteTrackState){
        when(state){
            is FavoriteTrackState.Empty -> {
                binding.apply {
                    emptyFavoriteText.visibility = View.VISIBLE
                    emptyFavoriteImage.visibility = View.VISIBLE
                    recyclerViewFavoriteTrack.visibility = View.GONE
                }

                binding.emptyFavoriteText.text = getString(R.string.empty_media_tracks)
                binding.emptyFavoriteImage.setImageResource(R.drawable.ic_not_found_120)
            }

            is FavoriteTrackState.Content -> {
                trackAdapter.notifyDataSetChanged()
                binding.apply {
                    emptyFavoriteText.visibility = View.GONE
                    emptyFavoriteImage.visibility = View.GONE
                    recyclerViewFavoriteTrack.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(): FavoriteTrackFragment{
            return FavoriteTrackFragment().apply {  }
        }
    }
}