package com.example.musicapp.logic.mediaPlayer

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.logic.song.Song

object AppExoPlayer{
    var player: ExoPlayer? = null
    val haveSongs = mutableStateOf(false)
    val isPlaying = mutableStateOf(false)

    fun createPlayer(context: Context){
        player = ExoPlayer.Builder(context).build()
    }

    fun addMediaItem(uri: Uri){
        val mediaItem = MediaItem.fromUri(uri)
        player?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    fun addPlaylist(songList: List<Song>){
        for (song in songList){
            player?.let {
                val mediaItem = MediaItem.fromUri(song.uri)
                it.addMediaItem(mediaItem)
            }
        }
        player?.let {
            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    fun playMusic(){
        player?.let {
            it.play()
            isPlaying.value = true
        }
    }

    fun stopMusic(){
        player?.let {
            it.pause()
            isPlaying.value = false
        }
    }
}