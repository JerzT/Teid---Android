package com.example.musicapp.logic.mediaPlayer


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ShuffleOrder.DefaultShuffleOrder
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.song.Song
import kotlin.random.Random

object AppExoPlayer{
    var player: ExoPlayer? = null
    val haveSongs = mutableStateOf(false)
    val currentSong = mutableStateOf<Song?>(null)
    val isPlaying = mutableStateOf(false)
    private var songList = mutableListOf<Song?>(null)
    val stateOfLoop = mutableIntStateOf(Player.REPEAT_MODE_OFF)

    fun createPlayer(context: Context){
        player = ExoPlayer.Builder(context).build()
        player?.addListener(object: Player.Listener{
            override fun onIsPlayingChanged(isPlayingExo: Boolean) {
                isPlaying.value = isPlayingExo
            }

            @OptIn(UnstableApi::class)
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                currentSong.value = songList[player!!.currentWindowIndex]
            }
        })
    }

    @SuppressLint("Range")
    private fun createMediaItem(
        song: Song,
        albumsList: List<Any>,
    ): MediaItem {
        currentSong.value = song
        val coverUri: Uri? = getImageFromAlbum(albumsList, currentSong.value!!)

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

    fun addSong(
        song: Song,
        albumsList: List<Any>
    ){
        val mediaItem = createMediaItem(song, albumsList)
        player?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    fun setPlaylist(
        songPlaylist: List<Song>,
        albumsList: List<Any>
    ){
        songList = songPlaylist.toMutableList()

        player?.let {
            it.clearMediaItems()
            it.repeatMode = Player.REPEAT_MODE_OFF

            for (song in songPlaylist){
                val mediaItem = createMediaItem(song, albumsList)
                it.addMediaItem(mediaItem)
            }

            it.prepare()
            playMusic()
            haveSongs.value = true
        }
    }

    private fun getImageFromAlbum(
        albumList: List<Any>,
        currentPlaying: Song,
    ): Uri? {
        for(album in albumList){
            when(album){
                is Album -> {
                    if(album.uri == currentPlaying.parentUri){
                        return album.cover
                    }
                }
                is List<*> ->{
                    val discs = album.filterIsInstance<Album>()
                    for (disc in discs) {
                        if (disc.uri == currentPlaying.parentUri) {
                            return disc.cover ?: discs.firstOrNull { it.cover != null }?.cover
                        }
                    }
                }
            }
        }

        return null
    }

    fun setPlaylistToSelectedSong(selectedSong: Song, songList: List<Song>){
        val index = songList.indexOf(selectedSong)
        currentSong.value = selectedSong
        player?.seekTo(index, 0)
    }

    fun loopSong(){
        player?.let{
            it.repeatMode = Player.REPEAT_MODE_ONE
            stateOfLoop.intValue = Player.REPEAT_MODE_ONE
        }
    }
    fun loopPlaylist(){
        player?.let {
            it.repeatMode = Player.REPEAT_MODE_ALL
            stateOfLoop.intValue = Player.REPEAT_MODE_ALL
        }
    }
    fun stopLoop(){
        player?.let {
            it.repeatMode = Player.REPEAT_MODE_OFF
            stateOfLoop.intValue = Player.REPEAT_MODE_OFF
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @OptIn(UnstableApi::class)
    fun shufflePlaylist(){
        player?.let {
            val listOfIndex: MutableList<Int> = mutableListOf()

            for(x in 0..<it.mediaItemCount){
                listOfIndex.add(x)
            }

            val randomSeed = Random.nextLong(0,100000)

            listOfIndex.shuffle()

            val indexOfCurrentItem = it.currentMediaItemIndex

            listOfIndex.remove(indexOfCurrentItem)
            listOfIndex.add(0, indexOfCurrentItem)

            it.setShuffleOrder(DefaultShuffleOrder(listOfIndex.toIntArray(), randomSeed))
            it.shuffleModeEnabled = !it.shuffleModeEnabled
        }
    }

    fun playMusic(){
        player?.play()
    }

    fun stopMusic(){
        player?.pause()
    }

    fun nextSong(){
        player?.seekToNext()
    }

    fun previousSong(){
        player?.seekToPrevious()
    }
}