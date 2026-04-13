package com.practicum.playlistmarket2

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
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
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarket2.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.mutableListOf

class TrackActivity : AppCompatActivity() {
    private var savedTrack: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.track_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedTrack = intent.getSerializableExtra(ITEM_TRACK) as? Track

        savedTrack?.let {
            findViewById<TextView>(R.id.trackName).text = it.trackName
            findViewById<TextView>(R.id.artistName).text = it.artistName
            findViewById<TextView>(R.id.trackTimeMillis).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)
            findViewById<TextView>(R.id.collectionName).text = it.collectionName
            findViewById<TextView>(R.id.releaseDate).text = it.releaseDate.take(4)
            findViewById<TextView>(R.id.primaryGenreName).text = it.primaryGenreName
            findViewById<TextView>(R.id.country).text = it.country

            val trackImage = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(getCoverArtwork(it))
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_312)
                .transform(RoundedCorners(dpToPx(8f,this)))
                .into(trackImage)
        }

        val buttonBack = findViewById<Button>(R.id.button_arrow)
        buttonBack.setOnClickListener {
            finish()
        }

    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    fun getCoverArtwork(track: Track) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVED_TRACK, savedTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
        // Достаем трек и отрисовываем его
        savedTrack = savedInstanceState.getSerializable(SAVED_TRACK) as? Track
        savedTrack?.let {
            findViewById<TextView>(R.id.trackName).text = it.trackName
            findViewById<TextView>(R.id.artistName).text = it.artistName
            findViewById<TextView>(R.id.trackTimeMillis).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)
            findViewById<TextView>(R.id.collectionName).text = it.collectionName
            findViewById<TextView>(R.id.releaseDate).text = it.releaseDate.take(4)
            findViewById<TextView>(R.id.primaryGenreName).text = it.primaryGenreName
            findViewById<TextView>(R.id.country).text = it.country

            val trackImage = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(getCoverArtwork(it))
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_312)
                .transform(RoundedCorners(dpToPx(8f,this)))
                .into(trackImage)
        }
    }

    companion object {
        const val HISTORY_PREFERENCES = "history_preferences"
        const val ITEM_TRACK = "item_track"
        const val SAVED_TRACK = "saved_track"
    }

}




