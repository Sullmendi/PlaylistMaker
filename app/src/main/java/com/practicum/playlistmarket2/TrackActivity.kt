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
import org.w3c.dom.Text
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
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeMillis: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var trackImage: ImageView
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.track_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedTrack = if (savedInstanceState != null) {
            savedInstanceState.getParcelable<Track>(SAVED_TRACK) as? Track
        } else {
            intent.getParcelableExtra<Track>(ITEM_TRACK) as? Track
        }

        trackName = findViewById<TextView>(R.id.trackName)
        artistName = findViewById<TextView>(R.id.artistName)
        trackTimeMillis = findViewById<TextView>(R.id.trackTimeMillis)
        collectionName = findViewById<TextView>(R.id.collectionName)
        releaseDate = findViewById<TextView>(R.id.releaseDate)
        primaryGenreName = findViewById<TextView>(R.id.primaryGenreName)
        country = findViewById<TextView>(R.id.country)
        trackImage = findViewById<ImageView>((R.id.trackImage))


        savedTrack?.let {
            trackName.text = it.trackName
            artistName.text = it.artistName
            trackTimeMillis.text = dateFormat.format(it.trackTimeMillis)
            collectionName.text = it.collectionName
            releaseDate.text = it.releaseDate.take(4)
            primaryGenreName.text = it.primaryGenreName
            country.text = it.country

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
        outState.putParcelable(SAVED_TRACK, savedTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        savedTrack = savedInstanceState.getParcelable(SAVED_TRACK) as? Track
    }

    companion object {
        const val HISTORY_PREFERENCES = "history_preferences"
        const val ITEM_TRACK = "item_track"
        const val SAVED_TRACK = "saved_track"
    }

}




