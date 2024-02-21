package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.fitCenter

class PlayerActivity : AppCompatActivity() {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        lateinit var selectedTrack: Track
        if (intent.getParcelableExtra<Track>(Constants.PARCELABLE_TO_PLAYER_KEY) != null) {
            selectedTrack = intent.getParcelableExtra(Constants.PARCELABLE_TO_PLAYER_KEY)!!
        } else {
            finish()
        }

        val artwork = findViewById<ImageView>(R.id.artwork)
        val roundingRadius = artwork.resources.getDimension(R.dimen.Artwork_rounding)
        val pixelsForRoundedCorners = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, roundingRadius, artwork.resources.displayMetrics)
        Glide.with(artwork)
            .load(selectedTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(),RoundedCorners(pixelsForRoundedCorners.toInt()))
            .into(artwork)
        Log.d("MyTag", "Pixel count is ${pixelsForRoundedCorners.toInt()}")
        val trackName = findViewById<TextView>(R.id.trackName)
        trackName.text = selectedTrack.trackName

        val artistName = findViewById<TextView>(R.id.artistName)
        artistName.text = selectedTrack.artistName

        val playlistAddButton = findViewById<ImageButton>(R.id.playlistAddButton)
        playlistAddButton.setOnClickListener() {
            //TODO
        }

        val playAndPauseButton = findViewById<ImageButton>(R.id.playAndPauseButton)
        playAndPauseButton.setOnClickListener() {
            //TODO
        }

        val favoriteButton = findViewById<ImageButton>(R.id.favoriteButton)
        favoriteButton.setOnClickListener() {
            //TODO
        }

        var playTimer = findViewById<TextView>(R.id.playTimer)

        val trackTime = findViewById<TextView>(R.id.trackTime)
        trackTime.text = selectedTrack.trackTime

        val collectionName = findViewById<TextView>(R.id.collectionName)
        collectionName.text = selectedTrack.collectionName
        if (selectedTrack.collectionName == "") {
            collectionName.visibility = View.GONE
        }

        val releaseDate = findViewById<TextView>(R.id.releaseDate)
        releaseDate.text = selectedTrack.releaseDate
        val primaryGenreName = findViewById<TextView>(R.id.primaryGenreName)
        primaryGenreName.text = selectedTrack.primaryGenreName

        val country = findViewById<TextView>(R.id.country)
        country.text = selectedTrack.country


        val backButton = findViewById<View>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }


    }


}
