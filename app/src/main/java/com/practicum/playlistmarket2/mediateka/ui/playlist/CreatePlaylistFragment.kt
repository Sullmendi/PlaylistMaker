package com.practicum.playlistmarket2.mediateka.ui.playlist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class CreatePlaylistFragment: Fragment() {
    val requester = PermissionRequester.instance()
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var _binding: FragmentCreatePlaylistBinding? = null
    val binding get() = _binding!!

    open val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(com.practicum.playlistmarket2.R.string.finish_create_title))
            .setMessage(getString(com.practicum.playlistmarket2.R.string.finish_create_message))
            .setNeutralButton(getString(com.practicum.playlistmarket2.R.string.finish_cancel)) { dialog, which ->
                dialog.dismiss()
            }.setNegativeButton(getString(com.practicum.playlistmarket2.R.string.finish_text)) { dialog, which ->
                dialog.dismiss()
                findNavController().popBackStack()
            }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveImageToPrivateStorage(uri)
                }
            }

        viewModel.playlistImageObserve.observe(viewLifecycleOwner) { imagePath ->
            if (!imagePath.isNullOrEmpty()) {
                binding.insidePlaylistImage.visibility = View.GONE
                val imageFile = File(imagePath)

                Glide.with(this)
                    .load(imageFile)
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
                    .placeholder(R.drawable.ic_add_photo_100)
                    .into(binding.playlistImage)
            } else {
                binding.insidePlaylistImage.visibility = View.VISIBLE
                binding.insidePlaylistImage.setImageResource(R.drawable.ic_add_photo_100)
            }
        }

        binding.playlistImage.setOnClickListener {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            lifecycleScope.launch {
                requester.request(permission).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                data = Uri.fromParts("package", requireContext().packageName, null)
                            }
                            requireContext().startActivity(intent)
                        }
                        is PermissionResult.Denied.NeedsRationale -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.rationale_permission),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is PermissionResult.Cancelled -> {
                            return@collect
                        }
                    }
                }
            }
        }

        viewModel.observeStateEnableButton().observe(viewLifecycleOwner){ isEnabled ->
            binding.buttonCreate.isEnabled = isEnabled
        }

        binding.editTextName.doOnTextChanged { text, start, before, count ->
            viewModel.makePlaylistName(text.toString())
        }

        binding.editTextDescription.doOnTextChanged { text, start, before, count ->
            viewModel.makePlaylistDescription(text.toString())
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(viewModel.isAnythingChange()){
                    showFinishMessage()
                } else{
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.buttonArrow.setOnClickListener {
            if(viewModel.isAnythingChange()){
                showFinishMessage()
            } else{
                findNavController().popBackStack()
            }
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.createPlaylist()
            Toast.makeText(requireContext(), "Плейлист "+ viewModel.playlistName +" создан", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

    }

    open fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
    fun showFinishMessage(){
        confirmDialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}