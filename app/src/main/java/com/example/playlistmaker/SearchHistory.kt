package com.example.playlistmaker

import java.util.ArrayList
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val preferences: SharedPreferences) {
    companion object {
        var recentTracksList = arrayListOf<Track>()
    }
    fun addTrackToRecent(newTrack: Track) {
        if (recentTracksList.none { it.trackId == newTrack.trackId }) {

            if (recentTracksList.size < 10) {
                recentTracksList.add(0, newTrack)
                encodeAndSave()
            } else {
                recentTracksList.removeAt(9)
                recentTracksList.add(0, newTrack)
                encodeAndSave()
            }
        }

        else {
            recentTracksList.remove(newTrack)
            recentTracksList.add(0, newTrack)
            encodeAndSave()
        }
    }
    fun encodeAndSave() {
        val tracks = recentTracksList
        val gson = Gson()
        val jsonString = gson.toJson(tracks)
        preferences.edit().putString(Constants.RECENT_TRACKS_KEY, jsonString).apply()
    }

    fun decodeAndLoad() {

        val jsonString = preferences.getString(Constants.RECENT_TRACKS_KEY, "empty")

        if (jsonString == "empty") {
            recentTracksList.clear()
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            val tracks = Gson().fromJson<ArrayList<Track>>(jsonString, type)
            recentTracksList = tracks
        }
    }
}








