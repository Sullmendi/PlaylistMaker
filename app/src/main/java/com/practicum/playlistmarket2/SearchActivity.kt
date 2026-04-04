package com.practicum.playlistmarket2

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarket2.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.mutableListOf

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
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        recyclerHistoryView = findViewById<RecyclerView>(R.id.recyclerHistoryViewTrack)
        recyclerHistoryView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        placeholderMessage = findViewById<LinearLayout>(R.id.placeholder_message)
        placeholderText = findViewById<TextView>(R.id.placeholder_text)
        placeholderImage = findViewById<ImageView>(R.id.placeholder_image)
        refreshButton = findViewById<Button>(R.id.refresh_button)
        cleanHistoryButton = findViewById<Button>(R.id.search_history_button)
        historyLayout = findViewById< LinearLayout>(R.id.search_history)

        trackAdapter = TrackAdapter(trackList) {track ->
            historyPref.addTrackToHistory(track)

            val updatedHistory = historyPref.readHistory()
            historyTrackList.clear()
            historyTrackList.addAll(updatedHistory)
            historyTrackAdapter.notifyDataSetChanged()
        }
        recyclerView.adapter = trackAdapter

        historyTrackAdapter = TrackAdapter(historyTrackList) {track ->
            historyPref.addTrackToHistory(track)

            val update = historyPref.readHistory()
            historyTrackList.clear()
            historyTrackList.addAll(update)
            historyTrackAdapter.notifyDataSetChanged()
        }
        recyclerHistoryView.adapter = historyTrackAdapter

        clearButton.setOnClickListener {
           searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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



        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query = searchEditText.text.toString()
                if(searchEditText.text.isNotEmpty()){
                    doSearch(query)
                }
                true
            } else {
            false
            }
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
        trackService.findTrack(query).enqueue(object: Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if (response.isSuccessful){
                    val results = response.body()?.trackResults
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
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}



