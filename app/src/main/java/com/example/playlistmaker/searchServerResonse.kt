package com.example.playlistmaker

data class searchServerResonse(
    val resultCount:Int,
    val results: List<unparsedTrack>
)

class unparsedTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId : Long,
    val collectionName : String?,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String
)