package com.practicum.playlistmarket2.mediateka.ui.one_playlist

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.mediateka.ui.playlist.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment: CreatePlaylistFragment() {

    companion object {
        const val ITEM_PLAYLIST_ID = "item_playlist_id"

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ITEM_PLAYLIST_ID to playlist.id)
    }

    override val viewModel: EditPlaylistViewModel by viewModel{
        parametersOf(arguments?.getLong(ITEM_PLAYLIST_ID) ?:0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.makePlaylistName(text.toString())
        }

        binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.makePlaylistDescription(text.toString())
        }

        binding.createTitle.text = getString(R.string.edit_title)
        binding.buttonCreate.text = getString(R.string.save_button)

        viewModel.observePlaylist().observe(viewLifecycleOwner){playlist ->

            binding.editTextName.setText(playlist.playlistName)
            binding.editTextDescription.setText(playlist.playlistDescription)
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.buttonArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.createPlaylist()
            findNavController().popBackStack()
        }

    }

}