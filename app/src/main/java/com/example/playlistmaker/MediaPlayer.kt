package com.example.playlistmaker

import android.media.MediaPlayer
import android.widget.ImageButton

class MediaPlayer(private val button: ImageButton, private val url: String?) {
    private val mediaPlayer = MediaPlayer()

    enum class MediaPlayerState {
        IDLE,
        INITIALIZED,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        PLAYBACK_COMPLETED,
        ERROR
    }

    var mediaPlayerState: MediaPlayerState = MediaPlayerState.IDLE
    val validURL = !url.isNullOrEmpty()

    fun preparePlayer() {
        if (validURL) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                button.isEnabled = true
                mediaPlayerState = MediaPlayerState.PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                mediaPlayerState = MediaPlayerState.PREPARED
                button.setImageResource(R.drawable.play_button)
            }
        }
    }

    private fun startPlayer() {
        if (validURL) {
            mediaPlayer.start()
            mediaPlayerState = MediaPlayerState.STARTED
        }
    }

    fun pausePlayer() {
        if (validURL) {
            mediaPlayer.pause()
            mediaPlayerState = MediaPlayerState.PAUSED
        }
    }

    fun releasePlayer() {
        if (validURL) {
            mediaPlayer.release()
        }
    }

    fun playbackControl() {
        if (validURL) {
            when (mediaPlayerState) {
                MediaPlayerState.STARTED, MediaPlayerState.PLAYBACK_COMPLETED -> {
                    pausePlayer()
                }

                MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                    startPlayer()
                }

                else -> {}
            }
        }
    }
    fun getCurrentPosition() : Int {
        return mediaPlayer.currentPosition
    }
    fun getDuration() : Int {
        return mediaPlayer.duration
    }

    fun getIsPlaying() : Boolean{
        return mediaPlayer.isPlaying()
    }
}