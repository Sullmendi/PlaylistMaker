package com.practicum.playlistmarket2.keeping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FavoriteTracksFragmentBinding
import com.practicum.playlistmarket2.keeping.domain.api.FavoriteTrackState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class FavoriteTrackFragment: Fragment() {
    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModel()

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
        viewModel.observeState().observe(this){
            render(it)
        }
    }
    fun render(state: FavoriteTrackState){
        binding.emptyFavoriteText.text = getString(R.string.empty_media_tracks)
        binding.emptyFavoriteImage.setImageResource(R.drawable.ic_not_found_120)
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