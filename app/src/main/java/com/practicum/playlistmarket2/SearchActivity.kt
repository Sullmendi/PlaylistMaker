package com.practicum.playlistmarket2

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var placeholderText: TextView
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrack)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter

        placeholderMessage = findViewById<LinearLayout>(R.id.placeholder_message)
        placeholderText = findViewById<TextView>(R.id.placeholder_text)
        placeholderImage = findViewById<ImageView>(R.id.placeholder_image)
        refreshButton = findViewById<Button>(R.id.refresh_button)


        val buttonBack = findViewById<Button>(R.id.button_arrow)
        buttonBack.setOnClickListener {
            finish()
        }
        val clearButton = findViewById<ImageButton>(R.id.clear_icon)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.requestFocus()
        var query = searchEditText.text.toString()

        clearButton.setOnClickListener {
           searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
        }

        searchEditText.doOnTextChanged { text, start, before, count ->
            clearButton.visibility = clearButtonVisibility(text)
            savedPersonText = text.toString()
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
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}



