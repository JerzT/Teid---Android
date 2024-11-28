package com.example.musicapp.logic.mediaPlayer

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import com.example.musicapp.logic.song.Song


object MediaPlayerApp {
    var mediaPlayer: MediaPlayer? = null
    val isPlaying = mutableStateOf(false)
    private var songList: MutableList<Song> = mutableListOf()
    private var isShuffled: Boolean = false
    var currentPlaying = mutableStateOf<Song?>(null)

    private fun createMediaPlayer(context: Context){
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        mediaPlayer?.setOnCompletionListener {
            nextSongPlay(context)
        }
        val serviceIntent: Intent = Intent(
            context,
            PlaybackService::class.java
        )
        context.startService(serviceIntent)
        mediaPlayerNotification(context = context)
    }

    fun addMusicToPlay(context: Context, song: Song) {
        if (mediaPlayer == null) {
            createMediaPlayer(context)
            mediaPlayer?.apply{
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
        isShuffled = false
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

    //it made specially for slider in ActuallyPlayingBar
    fun stopMusicButIsPlaying(){
        mediaPlayer?.let {
            if (it.isPlaying){
                it.pause()
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

    fun shuffleSongList(){
        songList.shuffle()
        isShuffled = true
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying.value = false
    }
}