package com.example.musicapp.ui.actuallyPlaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.image.albumCoverCache
import com.example.musicapp.logic.mediaPlayer.MediaPlayerApp
import com.example.musicapp.logic.song.Song

@Composable
fun ActuallyPlayingBar(
    albumList: List<Album>,
    modifier: Modifier
) {
    val currentPlaying = remember { MediaPlayerApp.currentPlaying }
    val image = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(currentPlaying.value) {
        currentPlaying.value?.let {
            image.value = getImageFromAlbum(albumList, it)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
    ) {
        image.value?.let {
            Image(
                painter = BitmapPainter(it),
                contentDescription = "Album Cover"
            )
        }
    }
}


private fun getImageFromAlbum(
    albumList: List<Album>,
    currentPlaying: Song,
): ImageBitmap? {
    val uri: String

    for(album in albumList){
        if(album.uri == currentPlaying.parentUri){
            uri = "${album.uri}"
            return albumCoverCache[uri]
        }
    }

    return null
}