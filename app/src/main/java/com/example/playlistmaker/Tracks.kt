package com.example.playlistmaker

var trackList = arrayListOf<Track>()
var recentTracksList = arrayListOf<Track>()
class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: Long
)
