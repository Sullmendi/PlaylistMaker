package com.practicum.playlistmarket2.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.databinding.FragmentSearchBinding
import com.practicum.playlistmarket2.player.ui.TrackFragment
import com.practicum.playlistmarket2.search.domain.api.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    var savedPersonText = ""
    private val binding get() = _binding!!
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner){
                render(it)
        }

        viewModel.observeIntent().observe(viewLifecycleOwner){ track ->
            if(track != null){
                findNavController().navigate(R.id.action_searchFragment_to_trackFragment,
                    TrackFragment.createArgs(track))
                viewModel.cleanTrack()
            }
            }

            binding.searchEditText.requestFocus()
            binding.recyclerViewTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.recyclerHistoryViewTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

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
                val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        placeholderImage.setImageDrawable(requireContext().getDrawable(R.drawable.ic_some_search_problem_120))
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
                        placeholderImage.setImageDrawable(requireContext().getDrawable(R.drawable.ic_not_found_120))
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
            const val CLICK_DEBOUNCE_DELAY = 1000L
        }
    }