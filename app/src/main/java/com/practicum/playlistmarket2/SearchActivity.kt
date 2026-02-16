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
class SearchActivity : AppCompatActivity() {
    private var personText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                personText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)


        if (savedInstanceState != null) {
            personText = savedInstanceState.getString("SEARCH_TEXT").toString()
            searchEditText.setText(personText)
        }


    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT",personText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        personText = savedInstanceState.getString("SEARCH_TEXT","")
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.setText(personText)
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }