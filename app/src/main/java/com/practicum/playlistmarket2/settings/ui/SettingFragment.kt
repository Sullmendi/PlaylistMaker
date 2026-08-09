package com.practicum.playlistmarket2.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.practicum.playlistmarket2.App
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val currentTheme = (requireActivity().application as App).isDarkThemeEnabled()
            binding.themeSwitch.isChecked = currentTheme

            viewModel.observeDarkTheme().observe(viewLifecycleOwner){ isDark ->
                binding.themeSwitch.isChecked = isDark
                (requireActivity().application as App).switchTheme(isDark)
            }

            binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.setTheme(checked)
            }

            binding.shareApp.setOnClickListener {
                shareApp()
            }

            binding.messageToSupport.setOnClickListener {
                writeToSupport()
            }

            binding.personalAgreement.setOnClickListener {
                openPersonalAgreement()
            }

        }

        fun shareApp() {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_android_class))

            val chooseApp = Intent.createChooser(shareIntent,null)
            startActivity(chooseApp)
        }

        fun writeToSupport() {
            val supportIntent = Intent()
            supportIntent.action = Intent.ACTION_SENDTO
            supportIntent.type = "text/plain"
            supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_text))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_theme))
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))

            val chooseMail = Intent.createChooser(supportIntent,null)
            try {
                startActivity(chooseMail)
            } catch (e: Exception){
                Toast.makeText(requireContext(), getString(R.string.mail_not_found), Toast.LENGTH_SHORT).show()
            }
        }

        fun openPersonalAgreement(){
            val personalAgreementIntent = Intent()
            personalAgreementIntent.action = Intent.ACTION_VIEW
            personalAgreementIntent.data = getString(R.string.url_personal_agreement).toUri()

            startActivity(personalAgreementIntent)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }