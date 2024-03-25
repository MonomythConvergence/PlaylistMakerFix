package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

var trackList = arrayListOf<Track>()
var recentTracksList = arrayListOf<Track>()
@Parcelize
class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName : String?,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String,
    val previewUrl : String?
): Parcelable