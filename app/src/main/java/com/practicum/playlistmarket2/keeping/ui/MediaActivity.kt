package com.practicum.playlistmarket2.keeping.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonArrow.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaTabAdapter(supportFragmentManager, lifecycle)
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlist)
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}