package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object MediaPlayerApp {
    private var mediaPlayer: MediaPlayer? = null
    val isPlaying = mutableStateOf(false)

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
                isPlaying.value = true
            }
        }
    }

    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying.value = false
            }
        }
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying.value = false
    }
}