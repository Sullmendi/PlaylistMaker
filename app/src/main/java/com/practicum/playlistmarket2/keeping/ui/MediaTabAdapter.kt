package com.practicum.playlistmarket2.keeping.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaTabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoriteTrackFragment.newInstance() else PlaylistFragment.newInstance()
    }

    override fun getItemCount(): Int {
        return 2
    }

}