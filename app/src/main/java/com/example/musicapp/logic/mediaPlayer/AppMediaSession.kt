package com.example.musicapp.logic.mediaPlayer

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession

object AppMediaSession{
    var mediaSession: MediaSession? = null

    fun setUpMediaSession(context: Context, player: ExoPlayer){
        mediaSession = MediaSession.Builder(context, player).build()
    }
}