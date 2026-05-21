package com.practicum.playlistmarket2.ui.track

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarket2.R
import com.practicum.playlistmarket2.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private var savedTrack: Track? = null
    private var trackPlayState = MEDIA_STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeMillis: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var trackImage: ImageView
    private lateinit var playButton: ImageButton
    private lateinit var playTime: TextView
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val mainHandler = Handler(Looper.getMainLooper())

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
        trackImage = findViewById<ImageView>(R.id.trackImage)
        playButton = findViewById<ImageButton>(R.id.buttonPlay)
        playTime = findViewById<TextView>(R.id.playTime)


        savedTrack?.let {
            trackName.text = it.trackName
            artistName.text = it.artistName
            trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)
            collectionName.text = it.collectionName
            releaseDate.text = it.releaseDate?.take(4)
            primaryGenreName.text = it.primaryGenreName
            country.text = it.country

            Glide.with(this)
                .load(getCoverArtwork(it))
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_312)
                .transform(RoundedCorners(dpToPx(8f, this)))
                .into(trackImage)
        }

        prepareMediaPlayer()

        playButton.setOnClickListener {
            playControl()
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

    fun getCoverArtwork(track: Track) = track.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_TRACK, savedTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        savedTrack = savedInstanceState.getParcelable(SAVED_TRACK) as? Track
    }

    private fun prepareMediaPlayer(){
        val url = savedTrack?.previewUrl
        if (!url.isNullOrEmpty()) {
            mediaPlayer.setDataSource(savedTrack?.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playButton.isEnabled = true
                trackPlayState = MEDIA_STATE_PREPARED
        }
        } else {
            playButton.isEnabled = false
            Toast.makeText(this, "Аудио отрывок песни недоступен", Toast.LENGTH_LONG).show()
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_button_play_100)
            trackPlayState = MEDIA_STATE_PREPARED
            mainHandler.removeCallbacks(updateTrackTime)
            playTime.setText(R.string.default_time)
        }
    }

    private fun startPlayMusic(){
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_button_stop_100)
        trackPlayState = MEDIA_STATE_PLAY
        mainHandler.post(updateTrackTime)
    }
    private fun pausePlayMusic(){
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_button_play_100)
        trackPlayState = MEDIA_STATE_PAUSE
        mainHandler.removeCallbacks(updateTrackTime)
    }

    private fun playControl(){
        when(trackPlayState){
            MEDIA_STATE_PLAY -> {
                pausePlayMusic()
            }
            MEDIA_STATE_PREPARED,MEDIA_STATE_PAUSE -> {
                startPlayMusic()
            }
        }
    }

    private val updateTrackTime = object : Runnable {
        override fun run() {
            if(trackPlayState == MEDIA_STATE_PLAY){

                if(mediaPlayer.isPlaying){
                    var updatedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    playTime.text = updatedTime
                    mainHandler.postDelayed(this,TIME_DELAY)
                } else{
                    playTime.text = getString(R.string.default_time)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(updateTrackTime)
        mediaPlayer.release()
    }

    companion object {
        const val HISTORY_PREFERENCES = "history_preferences"
        const val ITEM_TRACK = "item_track"
        const val SAVED_TRACK = "saved_track"
        const val MEDIA_STATE_DEFAULT = 0
        const val MEDIA_STATE_PREPARED = 1
        const val MEDIA_STATE_PLAY = 2
        const val MEDIA_STATE_PAUSE = 3
        const val TIME_DELAY = 300L
    }

}