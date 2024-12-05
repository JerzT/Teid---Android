package com.example.musicapp.logic.mediaPlayer


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.logic.database.setUpDatabase
import com.example.musicapp.logic.song.Song

object AppExoPlayer{
    var player: ExoPlayer? = null
    val haveSongs = mutableStateOf(false)
    val currentSong = mutableStateOf<Song?>(null)
    val isPlaying = mutableStateOf<Boolean>(false)

    fun createPlayer(context: Context){
        player = ExoPlayer.Builder(context).build()
        player!!.addListener(object: Player.Listener{
            override fun onIsPlayingChanged(isPlayingExo: Boolean) {
                isPlaying.value = isPlayingExo
            }
        })
    }

    @SuppressLint("Range")
    private fun createMediaItem(context: Context, song: Song): MediaItem {
        var coverUri: Uri? = null
        val db = setUpDatabase(context)
        val albumRequest = db.getAlbumFromUri(song.parentUri)
        albumRequest.use { albumRow ->
            while (albumRow.moveToNext()){
                coverUri = if(albumRow.getString(albumRow.getColumnIndex("cover")) != "")
                    albumRow.getString(albumRow.getColumnIndex("cover")).toUri() else null
            }
        }

        val mediaItem =
            MediaItem.Builder()
                .setMediaId("media-1")
                .setUri(song.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(song.author)
                        .setTitle(song.title)
                        .setArtworkUri(coverUri)
                        .build()
                )
                .build()
        return mediaItem
    }

    fun addSong(context: Context, song: Song){
        val mediaItem = createMediaItem(context, song)
        player?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    fun setPlaylist(context: Context, songList: List<Song>){
        player?.let {
            it.clearMediaItems()
            it.repeatMode = Player.REPEAT_MODE_ALL
        }
        for (song in songList){
            player?.let {
                val mediaItem = createMediaItem(context, song)
                it.addMediaItem(mediaItem)
            }
        }
        player?.let {
            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    fun setPlaylistToSelectedSong(selectedSong: Song, songList: List<Song>){
        val index = songList.indexOf(selectedSong)
        currentSong.value = selectedSong
        player?.seekTo(index, 0)
    }

    fun playMusic(){
        player?.play()
    }

    fun stopMusic(){
        player?.pause()
    }
}