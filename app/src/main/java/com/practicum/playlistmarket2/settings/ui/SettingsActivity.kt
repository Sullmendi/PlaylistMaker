package com.practicum.playlistmarket2.settings.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmarket2.App
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.ActivitySettingsBinding
import com.practicum.playlistmarket2.databinding.ActivityTrackBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentTheme = (application as App).isDarkThemeEnabled()
        binding.themeSwitch.isChecked = currentTheme

        viewModel.observeDarkTheme().observe(this){ isDark ->
            binding.themeSwitch.isChecked = isDark
            (application as App).switchTheme(isDark)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.setTheme(checked)
        }

        binding.buttonArrow.setOnClickListener {
            finish()
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
            Toast.makeText(this, getString(R.string.mail_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    fun openPersonalAgreement(){
        val personalAgreementIntent = Intent()
        personalAgreementIntent.action = Intent.ACTION_VIEW
        personalAgreementIntent.data = getString(R.string.url_personal_agreement).toUri()

        startActivity(personalAgreementIntent)
    }
}