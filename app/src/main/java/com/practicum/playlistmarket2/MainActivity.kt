package com.practicum.playlistmarket2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search_sign)
        val buttonMedia = findViewById<Button>(R.id.media)
        val buttonSetting = findViewById<Button>(R.id.settings)

        buttonSearch.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSetting.setOnClickListener {
            val settingIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingIntent)
        }

    }
}