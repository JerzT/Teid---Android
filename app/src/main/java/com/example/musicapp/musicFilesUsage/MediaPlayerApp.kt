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
    var songList: MutableList<Song> = mutableListOf()
    var currentPlaying = mutableStateOf<Song?>(null)

    fun addMusicToPlay(context: Context, song: Song) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(context, song.uri)
                    currentPlaying.value = song
                    prepare()
                }
            } else {
                mediaPlayer?.reset()
                mediaPlayer?.apply {
                    setDataSource(context, song.uri)
                    currentPlaying.value = song
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

    fun setAlbumAsPlaylist(albumsSongs: List<Song>){
        songList = albumsSongs.toMutableList()
    }

    fun nextSongPlay(
        context: Context
    ){
        if (songList.isEmpty()) return

        var index = 0
        if (currentPlaying.value != null){
            if (songList.indexOf(currentPlaying.value) != songList.size - 1){
                index = songList.indexOf(currentPlaying.value) + 1
            }
        }
        addMusicToPlay(
            context = context,
            song = songList[index]
        )
        playMusic()
    }

    fun previousSongPlay(
        context: Context
    ){
        if (songList.isEmpty()) return

        var index = songList.size - 1
        if(currentPlaying.value != null){
            if (songList.indexOf(currentPlaying.value) != 0){
                index = songList.indexOf(currentPlaying.value) - 1
            }
        }
        addMusicToPlay(
            context = context,
            song = songList[index]
        )
        playMusic()
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying.value = false
    }
}