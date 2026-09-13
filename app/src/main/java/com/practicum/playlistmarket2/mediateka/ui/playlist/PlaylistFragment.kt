package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmarket2.mediateka.domain.api.PlaylistState
import com.practicum.playlistmarket2.mediateka.ui.playlist.PlaylistViewModel
import com.practicum.playlistmarket2.player.ui.TrackFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner){ state ->
            render(state)
        }

        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistAdapter = PlaylistAdapter(viewModel.playlistList)
        binding.recyclerViewPlaylist.adapter = playlistAdapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }

    }
    fun render(state: PlaylistState){
        when(state) {
            is PlaylistState.Empty ->{
                binding.apply {
                    recyclerViewPlaylist.visibility = View.GONE
                    emptyPlaylistText.text = getString(R.string.empty_media_playlists)
                    emptyPlaylistText.visibility = View.VISIBLE
                    emptyPlaylistImage.setImageResource(R.drawable.ic_not_found_120)
                    emptyPlaylistImage.visibility = View.VISIBLE
                }
            }
            is PlaylistState.Content -> {
                playlistAdapter.notifyDataSetChanged()
                binding.apply {
                    recyclerViewPlaylist.visibility = View.VISIBLE
                    emptyPlaylistText.visibility = View.GONE
                    emptyPlaylistImage.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(): PlaylistFragment{
            return PlaylistFragment().apply {  }
        }
    }
}