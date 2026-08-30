package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.PlaylistsFragmentBinding
import com.practicum.playlistmarket2.mediateka.domain.api.PlaylistState
import com.practicum.playlistmarket2.mediateka.ui.playlist.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

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
        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }
    fun render(state: PlaylistState){
        binding.emptyPlaylistText.text = getString(R.string.empty_media_playlists)
        binding.emptyPlaylistImage.setImageResource(R.drawable.ic_not_found_120)
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