package com.example.musicapp.logic.mediaPlayer


//object AppExoPlayer{
//    var player: ExoPlayer? = null
//    val haveSongs = mutableStateOf(false)
//    val currentSong = mutableStateOf<Song?>(null)
//    val isPlaying = mutableStateOf(false)
//    private var songList = mutableListOf<Song?>(null)
//    val stateOfLoop = mutableIntStateOf(Player.REPEAT_MODE_OFF)
//    val stateOfShuffle = mutableStateOf(false)
//
//    fun createPlayer(context: Context){
//        player = ExoPlayer.Builder(context).build()
//
//        player?.addListener(object: Player.Listener{
//            override fun onIsPlayingChanged(isPlayingExo: Boolean) {
//                isPlaying.value = isPlayingExo
//            }
//
//            @OptIn(UnstableApi::class)
//            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
//                currentSong.value = songList[player!!.currentWindowIndex]
//            }
//        })
//    }
//
//    @SuppressLint("Range")
//    private fun createMediaItem(
//        song: Song,
//        albumsList: List<Any>,
//    ): MediaItem {
//        currentSong.value = song
//        val coverUri: Uri? = getImageFromAlbum(albumsList, currentSong.value!!)
//
//        val mediaItem =
//            MediaItem.Builder()
//                .setMediaId("media-1")
//                .setUri(song.uri)
//                .setMediaMetadata(
//                    MediaMetadata.Builder()
//                        .setArtist(song.author)
//                        .setTitle(song.title)
//                        .setArtworkUri(coverUri)
//                        .build()
//                )
//                .build()
//        return mediaItem
//    }
//
//    fun setPlaylist(
//        songPlaylist: List<Song>,
//        albumsList: List<Any>
//    ){
//        songList = songPlaylist.toMutableList()
//
//        player?.let {
//            it.clearMediaItems()
//            it.repeatMode = Player.REPEAT_MODE_OFF
//
//            for (song in songPlaylist){
//                val mediaItem = createMediaItem(song, albumsList)
//                it.addMediaItem(mediaItem)
//            }
//
//            it.prepare()
//            playMusic()
//            haveSongs.value = true
//        }
//    }
//
//    private fun getImageFromAlbum(
//        albumList: List<Any>,
//        currentPlaying: Song,
//    ): Uri? {
//        for(album in albumList){
//            when(album){
//                is Album -> {
//                    if(album.uri == currentPlaying.parentUri){
//                        return album.cover
//                    }
//                }
//                is List<*> ->{
//                    val discs = album.filterIsInstance<Album>()
//                    for (disc in discs) {
//                        if (disc.uri == currentPlaying.parentUri) {
//                            return disc.cover ?: discs.firstOrNull { it.cover != null }?.cover
//                        }
//                    }
//                }
//            }
//        }
//
//        return null
//    }
//
//    fun setPlaylistToSelectedSong(selectedSong: Song, songList: List<Song>){
//        val index = songList.indexOf(selectedSong)
//        currentSong.value = selectedSong
//        player?.seekTo(index, 0)
//    }
//
//    fun loopSong(){
//        player?.let{
//            it.repeatMode = Player.REPEAT_MODE_ONE
//            stateOfLoop.intValue = Player.REPEAT_MODE_ONE
//            handleChangingOfNotification()
//        }
//    }
//    fun loopPlaylist(){
//        player?.let {
//            it.repeatMode = Player.REPEAT_MODE_ALL
//            stateOfLoop.intValue = Player.REPEAT_MODE_ALL
//            handleChangingOfNotification()
//        }
//    }
//    fun stopLoop(){
//        player?.let {
//            it.repeatMode = Player.REPEAT_MODE_OFF
//            stateOfLoop.intValue = Player.REPEAT_MODE_OFF
//            handleChangingOfNotification()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
//    @OptIn(UnstableApi::class)
//    fun shufflePlaylist(){
//        player?.let {
//
//            if(it.shuffleModeEnabled){
//                it.shuffleModeEnabled = false
//                stateOfShuffle.value = false
//                handleChangingOfNotification()
//                return
//            }
//
//            val listOfIndex: MutableList<Int> = mutableListOf()
//
//            for(x in 0..<it.mediaItemCount){
//                listOfIndex.add(x)
//            }
//
//            val randomSeed = Random.nextLong(0,100000)
//
//            listOfIndex.shuffle()
//
//            val indexOfCurrentItem = it.currentMediaItemIndex
//
//            listOfIndex.remove(indexOfCurrentItem)
//            listOfIndex.add(0, indexOfCurrentItem)
//
//            it.setShuffleOrder(DefaultShuffleOrder(listOfIndex.toIntArray(), randomSeed))
//            it.shuffleModeEnabled = true
//            stateOfShuffle.value = true
//            handleChangingOfNotification()
//        }
//    }
//
//    fun handleChangingOfNotification(){
//        val session = AppMediaSession.mediaSession
//        session?.let {
//            val shuffleStateButton = if(stateOfShuffle.value) shuffleButton else noShuffleButton
//
//            val loopStateButton = when(stateOfLoop.intValue){
//                Player.REPEAT_MODE_OFF -> noLoopButton
//                Player.REPEAT_MODE_ONE -> loopAlbumButton
//                Player.REPEAT_MODE_ALL -> loopSongButton
//                else -> noLoopButton
//            }
//
//            it.setCustomLayout(ImmutableList.of(shuffleStateButton, loopStateButton))
//        }
//    }
//
//    fun playMusic(){
//        player?.play()
//    }
//
//    fun stopMusic(){
//        player?.pause()
//    }
//
//    fun nextSong(){
//        player?.seekToNext()
//    }
//
//    fun previousSong(){
//        player?.seekToPrevious()
//    }
//}