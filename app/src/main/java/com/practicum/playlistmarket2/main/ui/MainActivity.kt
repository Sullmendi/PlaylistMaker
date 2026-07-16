package com.practicum.playlistmarket2.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.ActivityMainBinding
import com.practicum.playlistmarket2.keeping.ui.MediaActivity
import com.practicum.playlistmarket2.search.ui.SearchActivity
import com.practicum.playlistmarket2.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val basePadding = resources.getDimensionPixelSize(R.dimen.distance_sixteen)
            v.setPadding(
                systemBars.left + basePadding,
                systemBars.top + basePadding,
                systemBars.right + basePadding,
                systemBars.bottom + basePadding
            )
            insets
        }

        binding.searchSign.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.media.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        binding.settings.setOnClickListener {
            val settingIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingIntent)
        }

    }
}