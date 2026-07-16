package com.practicum.playlistmarket2.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.ActivitySearchBinding
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.player.ui.TrackActivity
import com.practicum.playlistmarket2.search.domain.api.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    var savedPersonText: String = ""
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this){
            render(it)
        }

        viewModel.observeIntent().observe(this){ track ->
            val trackIntent = Intent(this, TrackActivity::class.java).apply {
                putExtra(ITEM_TRACK, track)
            }
            startActivity(trackIntent)
        }

        binding.buttonArrow.setOnClickListener {
            finish()
        }

        binding.searchEditText.requestFocus()
        binding.recyclerViewTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerHistoryViewTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(viewModel.trackList) { track ->
            viewModel.openTrack(track)
        }
        binding.recyclerViewTrack.adapter = trackAdapter

        historyTrackAdapter = TrackAdapter(viewModel.historyTrackList) { track ->
            viewModel.openTrack(track)

            val updatedHistory = viewModel.readHistory()
            viewModel.historyTrackList.clear()
            viewModel.historyTrackList.addAll(updatedHistory)
            historyTrackAdapter.notifyDataSetChanged()
        }
        binding.recyclerHistoryViewTrack.adapter = historyTrackAdapter

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            showHistoryVisible(true)
        }

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && savedPersonText == "") {
                viewModel.showHistory()
            } else {
                binding.searchHistory.visibility = View.GONE
            }
        }

        binding.searchEditText.doOnTextChanged { text, start, before, count ->
            val query = text.toString()
            savedPersonText = query
            binding.clearIcon.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
            if(query.isNotEmpty()) {
                viewModel.searchDebounce(changedText = query)
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.refreshButton.setOnClickListener {
            viewModel.lastSearchRetry()
        }
    }

   private fun showHistoryVisible(isVisible: Boolean) {
       if (isVisible) {
           binding.apply {
               searchHistory.visibility = View.VISIBLE
               placeholderMessage.visibility = View.GONE
               recyclerViewTrack.visibility = View.GONE
           }
           val updatedHistory = viewModel.readHistory()
           viewModel.historyTrackList.clear()
           viewModel.historyTrackList.addAll(updatedHistory)
           historyTrackAdapter.notifyDataSetChanged()
       } else {
           binding.searchHistory.visibility = View.GONE

       }
   }
    override fun onDestroy() {
        super.onDestroy()
    }
    fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    recyclerViewTrack.visibility = View.GONE
                    placeholderMessage.visibility = View.GONE
                }
                showHistoryVisible(false)
            }
            is SearchState.ContentSearch -> {
                binding.apply {
                    recyclerViewTrack.visibility = View.VISIBLE
                    placeholderMessage.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                showHistoryVisible(false)
                viewModel.trackList.clear()
                viewModel.trackList.addAll(state.trackList)
                trackAdapter.notifyDataSetChanged()
            }
            is SearchState.Error -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    recyclerViewTrack.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE
                    placeholderText.text = getString(R.string.search_problem)
                    placeholderImage.setImageDrawable(getDrawable(R.drawable.ic_some_search_problem_120))
                    refreshButton.visibility = View.VISIBLE
                }
                showHistoryVisible(false)

            }
            is SearchState.Empty -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    recyclerViewTrack.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE
                    placeholderText.text = getString(R.string.not_found)
                    placeholderImage.setImageDrawable(getDrawable(R.drawable.ic_not_found_120))
                    refreshButton.visibility = View.GONE
                }
                showHistoryVisible(false)
            }
            is SearchState.History -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    placeholderMessage.visibility = View.GONE
                }
                showHistoryVisible(true)
            }
            is SearchState.emptyHistory -> showHistoryVisible(false)
        }
    }

    companion object {
        const val ITEM_TRACK = "item_track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,savedPersonText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        savedPersonText = savedInstanceState.getString(SEARCH_TEXT,"")
        binding.searchEditText.setText(savedPersonText)
    }
}