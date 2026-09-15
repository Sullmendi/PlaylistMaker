package com.practicum.playlistmarket2.mediateka.ui.one_playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FragmentChoosePlaylistBinding
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.player.ui.TrackFragment
import com.practicum.playlistmarket2.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ChoosePlaylistFragment: Fragment() {

    companion object{
        const val ITEM_PLAYLIST_ID = "item_playlist_id"

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ITEM_PLAYLIST_ID to playlist.id)
    }

    private lateinit var trackAdapter: TrackAdapter
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    var trackCount: Int = 0

    private val viewModel by viewModel<ChoosePlaylistViewModel>{
        parametersOf(arguments?.getLong(ITEM_PLAYLIST_ID) ?: 0L)
    }
    private var _binding: FragmentChoosePlaylistBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosePlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(com.practicum.playlistmarket2.R.string.delete_playlist_bottom))
            .setMessage(getString(com.practicum.playlistmarket2.R.string.delete_playlist))
            .setNeutralButton(getString(com.practicum.playlistmarket2.R.string.finish_cancel)) { dialog, which ->
                dialog.dismiss()
            }.setNegativeButton(getString(com.practicum.playlistmarket2.R.string.delete)) { dialog, which ->
                viewModel.deletePlaylist()
                dialog.dismiss()
                findNavController().popBackStack()
            }

        binding.sharingButton.setOnClickListener {
            if (trackCount != 0) {
                val trackCountText = resources.getQuantityString(
                    R.plurals.tracks_count,
                    trackCount,
                    trackCount
                )
                viewModel.makeShareMessage(trackCountText)
            } else {
                Toast.makeText(requireContext(), getString(R.string.empty_playlist), Toast.LENGTH_SHORT).show()
            }
        }

        val bottomSheetContainerInformation = view.findViewById<LinearLayout>(R.id.menu_bottom_sheet)
        val bottomSheetBehaviorInformation = BottomSheetBehavior.from(bottomSheetContainerInformation).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehaviorInformation.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        _binding?.overlay?.visibility = View.GONE
                    }
                    else -> {
                        _binding?.overlay?.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                _binding?.overlay?.alpha = slideOffset.coerceIn(1f, 2f)
            }
        })

        binding.settingButton.setOnClickListener {
            bottomSheetBehaviorInformation.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.sharePlaylist.setOnClickListener {
            bottomSheetBehaviorInformation.state = BottomSheetBehavior.STATE_HIDDEN

            if (trackCount != 0) {
                val trackCountText = resources.getQuantityString(
                    R.plurals.tracks_count,
                    trackCount,
                    trackCount
                )
                viewModel.makeShareMessage(trackCountText)
            } else {
                Toast.makeText(requireContext(), getString(R.string.empty_playlist), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.observeEditIntent().observe(viewLifecycleOwner){playlist ->
            if (playlist != null){
                findNavController().navigate(R.id.action_choosePlaylistFragment_to_editPlaylistFragment,
                    EditPlaylistFragment.createArgs(playlist))
                viewModel.cleanEdit()
            }
        }

        binding.editPlaylist.setOnClickListener {
            bottomSheetBehaviorInformation.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.editPlaylist()
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetBehaviorInformation.state = BottomSheetBehavior.STATE_HIDDEN
            confirmDialog.show()
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehaviorInformation.state = BottomSheetBehavior.STATE_HIDDEN
        }


        viewModel.observeShare().observe(viewLifecycleOwner){ shareMessage ->
            sharePlaylist(shareMessage)
        }

        binding.buttonArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        trackAdapter = TrackAdapter(emptyList(),
            { track -> viewModel.openTrack(track) },{ track -> showDeleteDialog(track) }
        )
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTracks.adapter = trackAdapter

        viewModel.observeIntent().observe(viewLifecycleOwner){ track ->
            if(track != null){
                findNavController().navigate(R.id.action_choosePlaylistFragment_to_trackFragment,
                    TrackFragment.createArgs(track))
                viewModel.cleanTrack()
            }
        }

        val bottomSheetContainer = view.findViewById<LinearLayout>(R.id.standard_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        val mainContainer: View = view.findViewById(R.id.choose_playlist)
        val settingButton: View = view.findViewById(R.id.setting_button)

        mainContainer.post {
            val buttonLocation = IntArray(2)
            settingButton.getLocationOnScreen(buttonLocation)

            val buttonBottomY = buttonLocation[1] + settingButton.height

            val containerLocation = IntArray(2)
            mainContainer.getLocationOnScreen(containerLocation)
            val containerBottomY = containerLocation[1] + mainContainer.height

            var availableHeight = containerBottomY - buttonBottomY

            val offsetInPx = (24 * view.resources.displayMetrics.density).toInt()
            availableHeight -= offsetInPx

            bottomSheetBehavior.peekHeight = availableHeight
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner){ playlist ->
            trackCount = playlist.tracksCount
            playlist?.let{
                binding.playlistName.text = it.playlistName
                binding.playlistDescription.text = it.playlistDescription
                binding.trackCount.text = requireContext().resources.getQuantityString(
                    R.plurals.tracks_count,
                    it.tracksCount,
                    it.tracksCount
                )

                binding.playlistNameBottom.text = it.playlistName
                binding.trackCountBottom.text = requireContext().resources.getQuantityString(
                    R.plurals.tracks_count,
                    it.tracksCount,
                    it.tracksCount
                )

                if(!it.playlistImagePath.isNullOrEmpty()){
                    val imageFile = File(it.playlistImagePath)
                    Glide.with(this)
                        .load(imageFile)
                        .placeholder(R.drawable.ic_placeholder_312)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
                        .into(binding.playlistImage)
                    Glide.with(this)
                        .load(imageFile)
                        .placeholder(R.drawable.ic_placeholder_45)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(2f, requireContext())))
                        .into(binding.playlistImageBottom)
                } else{
                    Glide.with(this)
                        .load(R.drawable.ic_placeholder_312)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
                        .into(binding.playlistImage)
                    Glide.with(this)
                        .load(R.drawable.ic_placeholder_45)
                        .centerCrop()
                        .transform(RoundedCorners(dpToPx(2f, requireContext())))
                        .into(binding.playlistImageBottom)
                }
            }
        }

        viewModel.observeTracks().observe(viewLifecycleOwner){tracks ->
            trackAdapter.updateData(tracks)
            trackCount = tracks.size
            val durationSum = (tracks.sumOf { it.trackTimeMillis }/60000).toInt()
            binding.playlistTime.text = requireContext().resources.getQuantityString(
                R.plurals.tracks_time,
                durationSum,
                durationSum
            )
            if(tracks.isNotEmpty()){
                binding.noTracks.visibility = View.GONE
            } else{
                binding.noTracks.visibility = View.VISIBLE
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDeleteDialog(track: Track){
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setNeutralButton(getString(R.string.answer_nope)) { dialog, which ->
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.answer_yes)) { dialog, which ->
                viewModel.deleteTrack(track)
                dialog.dismiss()
            }
        confirmDialog.show()
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    fun sharePlaylist(message: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        val chooseApp = Intent.createChooser(shareIntent,"Share APK")
        startActivity(chooseApp)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylist()
    }

}
