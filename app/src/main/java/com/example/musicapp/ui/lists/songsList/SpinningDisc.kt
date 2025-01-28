package com.example.musicapp.ui.lists.songsList

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.image.albumCoverCache
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.song.Song

@SuppressLint("UnrememberedAnimatable")
@Composable
fun SpinningDisc(
    albumsList: List<Any>,
    listOfUri: List<String>,
    song: Song?,
    modifier: Modifier,
){
    val image = remember { mutableStateOf<ImageBitmap?>(null) }
    val album = remember { mutableStateOf<Any?>(null) }
    val isPlaying = remember { AppExoPlayer.isPlaying }
    val rotationAngleStopState = remember { mutableFloatStateOf(0f) }
    val rotationAngle = remember { mutableFloatStateOf(0f) }
    val songPlayed = remember { AppExoPlayer.currentSong }
    val isInSameAlbum = remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation = if (!isPlaying.value) {
        rotationAngleStopState.floatValue
    } else if(isInSameAlbum.value) {
        infiniteTransition.animateFloat(
            initialValue = rotationAngle.floatValue,
            targetValue = 360f + rotationAngle.floatValue,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 8000,
                    easing = LinearEasing,
                ),
            ),
            label = ""
        ).value
    } else {
        rotationAngleStopState.floatValue
    }

    LaunchedEffect(isPlaying.value) {
        rotationAngle.floatValue = rotationAngleStopState.floatValue
    }

    LaunchedEffect(rotation) {
        rotationAngleStopState.floatValue = rotation
        if(rotation == 360f){
            rotationAngle.floatValue = 0f
        }
    }

    LaunchedEffect(songPlayed.value) {
        for (uri in listOfUri){
            when(val a = album.value){
                is Album -> {
                    isInSameAlbum.value = uri.toUri() == a.uri
                }
                is List<*> -> {
                    val discList = a as List<Album>
                    isInSameAlbum.value = discList.any { uri.toUri() == it.uri }
                }
            }
        }
    }

    LaunchedEffect(song) {
        song?.let {
            album.value = getActuallyPlayedAlbum(albumsList, song)
            album.value?.let {
                image.value = getImageFromAlbum(it, song)
                for (uri in listOfUri) {
                    when(val a = album.value) {
                        is Album -> {
                            isInSameAlbum.value = uri.toUri() == songPlayed.value?.parentUri
                        }
                        is List<*> -> {
                            val discList = a as List<Album>
                            isInSameAlbum.value = discList.any { uri.toUri() == songPlayed.value?.parentUri }
                        }
                    }
                }
            }
        }
    }

    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
            .rotate(rotation)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            image.value?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = "Disc",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { shadowElevation = 8.dp.toPx() }
                )
            }
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                border = BorderStroke(4.dp, MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .size(68.dp)
                    .align(Alignment.Center)
            ){}
        }
    }
}

private fun getImageFromAlbum(
    album: Any,
    currentPlaying: Song,
): ImageBitmap? {
    return when (album) {
        is Album -> {
            if (album.uri == currentPlaying.parentUri) {
                albumCoverCache["${album.uri}"]
            } else null
        }
        is List<*> -> {
            (album as List<Album>).firstOrNull { it.uri == currentPlaying.parentUri }
                ?.let { albumCoverCache["${it.uri}"] }
        }
        else -> null
    }
}

private fun getActuallyPlayedAlbum(
    albumList: List<Any>,
    currentPlaying: Song,
): Any? {
    return albumList.firstOrNull { album ->
        when (album) {
            is Album -> album.uri == currentPlaying.parentUri
            is List<*> -> (album as List<Album>).any { it.uri == currentPlaying.parentUri }
            else -> false
        }
    }
}