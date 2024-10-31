package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

object MediaPlayerApp {
    private var mediaPlayer: MediaPlayer? = null

    fun addMusicToPlay(context: Context, uri: Uri) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(context, uri)
                    prepare()
                }
            } else {
                mediaPlayer?.reset()
                mediaPlayer?.apply {
                    setDataSource(context, uri)
                    prepare()
                }
            }
        } catch (e: Exception) {
            Log.e("MediaPlayerApp", "Error initializing MediaPlayer: ${e.message}")
        }
    }

    fun playMusic() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying ?: false

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}