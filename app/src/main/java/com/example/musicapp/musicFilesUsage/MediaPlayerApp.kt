package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri

object MediaPlayerApp: MediaPlayer() {
    fun addMusicToPlay(
        context: Context,
        uri: Uri
    ){
        this.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, uri)
            prepare()
        }
    }

    fun playMusic(){
        this.start()
    }

    fun stopMusic(){
        this.pause()
    }
}