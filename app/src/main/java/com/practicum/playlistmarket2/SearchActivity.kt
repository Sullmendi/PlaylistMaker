package com.practicum.playlistmarket2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {
    var savedPersonText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonBack = findViewById<Button>(R.id.button_arrow)
        buttonBack.setOnClickListener {
            finish()
        }
        val clearButton = findViewById<ImageButton>(R.id.clear_icon)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.requestFocus()

        clearButton.setOnClickListener {
           searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.doOnTextChanged { text, start, before, count ->
            clearButton.visibility = clearButtonVisibility(text)
            savedPersonText = text.toString()
        }

        if (savedInstanceState != null) {
            savedPersonText = savedInstanceState.getString(SEARCH_TEXT).toString()
            searchEditText.setText(savedPersonText)
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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }


    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }