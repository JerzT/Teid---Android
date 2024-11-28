package com.example.musicapp.logic.mediaPlayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

const val ACTION_PLAY: String = "com.example.action.play"

class PlaybackService: Service(), MediaPlayer.OnPreparedListener {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action: String? = intent.action

        when(action){
            ACTION_PLAY -> {
                if (MediaPlayerApp.isPlaying.value){
                    MediaPlayerApp.stopMusic()
                }
                else{
                    MediaPlayerApp.playMusic()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onPrepared(p0: MediaPlayer?) {
        return
    }

    override fun onDestroy() {
        super.onDestroy()

        MediaPlayerApp.releasePlayer()
    }
}