package com.example.musicapp.ui.bottomBar

import android.util.Log
import androidx.media3.common.Player
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer

fun handleLooping(){
    when(AppExoPlayer.player?.repeatMode){
        //0
        Player.REPEAT_MODE_OFF -> AppExoPlayer.loopAlbum()
        //2
        Player.REPEAT_MODE_ALL -> AppExoPlayer.loopSong()
        //1
        Player.REPEAT_MODE_ONE -> AppExoPlayer.stopLoop()
    }
}