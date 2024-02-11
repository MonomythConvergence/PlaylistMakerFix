package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {
    companion object {
        var darkTheme: Boolean = false
        lateinit var recentTracksSharedPreferences: SharedPreferences
    }
    private lateinit var themeSharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        themeSharedPreferences = getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)
        darkTheme=themeSharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
        switchTheme(darkTheme)

        recentTracksSharedPreferences = getSharedPreferences(Constants.RECENT_TRACKS_KEY, MODE_PRIVATE)
        SearchHistory(recentTracksSharedPreferences).decodeAndLoad()

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        themeSharedPreferences.edit().putBoolean(Constants.THEME_PREF_KEY,darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })

    }
}

