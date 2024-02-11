package com.example.playlistmaker

import java.util.ArrayList
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(private val preferences: SharedPreferences) {

    fun addTrackToRecent(newTrack: Track) {
        val existingTrack = recentTracksList.find { it.trackId == newTrack.trackId }

        if (existingTrack == null) {
            if (recentTracksList.size < 10) {
                recentTracksList.add(0, newTrack)
            } else {
                while (recentTracksList.size>9){
                    recentTracksList.removeAt(recentTracksList.size-1)
                }
                recentTracksList.add(0, newTrack)
            }
        }
        if (existingTrack != null) {
            recentTracksList.remove(existingTrack)
            recentTracksList.add(0, newTrack)
        }
        encodeAndSave()
        SearchActivity.recentAdapter.notifyDataSetChanged()
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








