package com.practicum.playlistmarket2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import androidx.core.view.updatePadding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val buttonBack = findViewById<Button>(R.id.button_arrow)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonShareApp = findViewById<FrameLayout>(R.id.share_app)
        buttonShareApp.setOnClickListener {
            shareApp()
        }

        val buttonSupport = findViewById<FrameLayout>(R.id.message_to_support)
        buttonSupport.setOnClickListener {
            writeToSupport()
        }

        val buttonPersonalAgreement = findViewById<FrameLayout>(R.id.personal_agreement)
        buttonPersonalAgreement.setOnClickListener {
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