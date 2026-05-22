package com.practicum.playlistmarket2.ui.activescreens

import android.content.Intent
import android.graphics.drawable.Drawable
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.data.network.HistoryPreferences
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.data.dto.TrackDto
import com.practicum.playlistmarket2.ui.track.TrackActivity
import com.practicum.playlistmarket2.ui.activescreens.TrackAdapter
import com.practicum.playlistmarket2.data.dto.TrackResponse
import com.practicum.playlistmarket2.data.network.TrackItunesApi
import com.practicum.playlistmarket2.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    var savedPersonText: String = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_SONG_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackItunesApi::class.java)
    var trackList = mutableListOf<Track>()
    private lateinit var historyTrackList: MutableList<Track>
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var placeholderText: TextView
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshButton: Button
    private lateinit var recyclerHistoryView: RecyclerView
    private lateinit var cleanHistoryButton: Button
    private lateinit var historyLayout: LinearLayout
    private lateinit var historyPref: HistoryPreferences
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchRunnable: kotlinx.coroutines.Runnable
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        historyTrackList = mutableListOf<Track>()
        val pref = getSharedPreferences(HISTORY_PREFERENCES,MODE_PRIVATE)
        historyPref = HistoryPreferences(pref)
        historyTrackList.addAll(historyPref.readHistory())

        val buttonBack = findViewById<Button>(R.id.button_arrow)
        buttonBack.setOnClickListener {
            finish()
        }
        val clearButton = findViewById<ImageButton>(R.id.clear_icon)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.requestFocus()
        var query = searchEditText.text.toString()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrack)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerHistoryView = findViewById<RecyclerView>(R.id.recyclerHistoryViewTrack)
        recyclerHistoryView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        placeholderMessage = findViewById<LinearLayout>(R.id.placeholder_message)
        placeholderText = findViewById<TextView>(R.id.placeholder_text)
        placeholderImage = findViewById<ImageView>(R.id.placeholder_image)
        refreshButton = findViewById<Button>(R.id.refresh_button)
        cleanHistoryButton = findViewById<Button>(R.id.search_history_button)
        historyLayout = findViewById<LinearLayout>(R.id.search_history)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        searchRunnable = Runnable{ doSearch(query) }

        trackAdapter = TrackAdapter(trackList) { track ->
            historyPref.addTrackToHistory(track)

            val updatedHistory = historyPref.readHistory()
            historyTrackList.clear()
            historyTrackList.addAll(updatedHistory)
            historyTrackAdapter.notifyDataSetChanged()

            sendIntent(track)
        }
        recyclerView.adapter = trackAdapter

        historyTrackAdapter = TrackAdapter(historyTrackList) { track ->
            historyPref.addTrackToHistory(track)

            val update = historyPref.readHistory()
            historyTrackList.clear()
            historyTrackList.addAll(update)
            historyTrackAdapter.notifyDataSetChanged()

            sendIntent(track)
        }
        recyclerHistoryView.adapter = historyTrackAdapter

        clearButton.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
           searchEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            trackList.clear()
            trackAdapter.notifyDataSetChanged()

                showHistory()
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            val history = historyPref.readHistory()
            if (hasFocus && searchEditText.text.isEmpty() && history.isNotEmpty()) {
                showHistory()
            } else {
                historyLayout.visibility = View.GONE
            }
        }

        searchEditText.doOnTextChanged { text, start, before, count ->
            query = text.toString()
            if(!text.isNullOrEmpty()){
                progressBar.visibility = View.VISIBLE
                placeholderMessage.visibility = View.GONE
                recyclerView.visibility = View.GONE
                searchDebounce()
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            } else{
                progressBar.visibility = View.GONE
            }

            clearButton.visibility = clearButtonVisibility(text)
            savedPersonText = text.toString()

            if (searchEditText.hasFocus() && text?.isEmpty() == true) {
                showHistory()
            } else {
                historyLayout.visibility = View.GONE
        }
        }
        if (savedInstanceState != null) {
            savedPersonText = savedInstanceState.getString(SEARCH_TEXT).toString()
            searchEditText.setText(savedPersonText)
        }

        cleanHistoryButton.setOnClickListener {
            historyPref.cleanHistory()
            historyTrackList.clear()
            historyTrackAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
        }

        if (searchEditText.text.isEmpty()) {
            showHistory()
        }
    }

    private fun showHistory() {
        val history = historyPref.readHistory()
        if (history.isNotEmpty()) {
            historyTrackList.clear()
            historyTrackList.addAll(history)
            historyTrackAdapter.notifyDataSetChanged()

            historyLayout.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            recyclerView.visibility = View.GONE
        } else{
            historyLayout.visibility = View.GONE
            return
        }
    }


    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,savedPersonText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        savedPersonText = savedInstanceState.getString(SEARCH_TEXT,"")
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.setText(savedPersonText)
        }

    fun showPlaceholderMessage(text: String, image: Drawable?, showRefreshButton: Boolean){
        if(text.isNotEmpty()) {
            recyclerView.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderText.text = text
            placeholderImage.setImageDrawable(image)
            refreshButton.visibility = if (showRefreshButton) View.VISIBLE else View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
        }
    }

    fun doSearch(query:String){
        historyLayout.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        trackService.findTrack(query).enqueue(object: Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful){
                    val results = response.body()?.trackResults?.map {
                        Track(it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl) }
                    trackList.clear()
                    if(!results.isNullOrEmpty()){
                        trackList.addAll(results)
                        showPlaceholderMessage("",null,false)
                    }
                    if(trackList.isEmpty()){
                        showPlaceholderMessage(getString(R.string.not_found), getDrawable(R.drawable.ic_not_found_120), false )
                    } else{
                        showPlaceholderMessage("",null,false)
                    }
                    trackAdapter.notifyDataSetChanged()
                } else{
                    errorSearch(query)
                }
            }
            override fun onFailure(
                call: Call<TrackResponse>,
                t: Throwable
            ) {
                progressBar.visibility = View.GONE
                errorSearch(query)
            }
        })
    }

    fun errorSearch(query:String){
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        showPlaceholderMessage(getString(R.string.search_problem), getDrawable(R.drawable.ic_some_search_problem_120), true)
        refreshButton.setOnClickListener { doSearch(query) }
    }


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val BASE_SONG_URL = "https://itunes.apple.com/"
        const val HISTORY_PREFERENCES = "history_preferences"
        const val ITEM_TRACK = "item_track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable,SEARCH_DEBOUNCE_DELAY)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun sendIntent(track: Track){
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
            val trackIntent = Intent(this@SearchActivity, TrackActivity::class.java).apply {
                putExtra(ITEM_TRACK, track)
            }
            startActivity(trackIntent)
        }
    }
}
