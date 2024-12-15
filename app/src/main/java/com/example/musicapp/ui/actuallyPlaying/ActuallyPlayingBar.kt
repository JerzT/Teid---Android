package com.example.musicapp.ui.actuallyPlaying

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.Screen
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.image.albumCoverCache
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.song.Song
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun ActuallyPlayingBar(
    albumList: List<Any>,
    navController: NavController,
    modifier: Modifier,
) {
    val image = remember { mutableStateOf<ImageBitmap?>(null) }
    val progress = remember { mutableFloatStateOf(
        AppExoPlayer.player!!.currentPosition.toFloat() /
                AppExoPlayer.player!!.duration.toFloat()) }
    val durationText = remember { mutableStateOf("0:00") }
    val currentPositionText = remember { mutableStateOf("0:00") }
    val currentSong = remember { AppExoPlayer.currentSong }
    val isPlaying = remember { AppExoPlayer.isPlaying }
    val actuallyPlayedAlbum = remember { currentSong.value?.let {
        getActuallyPlayedAlbum(albumList, it)
    }}

    LaunchedEffect(currentSong.value) {

        image.value = currentSong.value?.let {
            getImageFromAlbum(
                album = actuallyPlayedAlbum!!,
                currentPlaying = it
            )
        }


        progress.floatValue = AppExoPlayer.player!!.currentPosition.toFloat() /
                AppExoPlayer.player!!.duration.toFloat()

        val durationMinutes = currentSong.value?.length!! / 1000 / 60
        val durationSeconds = currentSong.value?.length!! / 1000 % 60

        durationText.value = String.format("%d:%02d", durationMinutes, durationSeconds)
    }

    LaunchedEffect(isPlaying.value) {
        while (isPlaying.value){
            //slider update
            progress.floatValue =
                AppExoPlayer.player!!.currentPosition.toFloat() /
                        AppExoPlayer.player!!.duration.toFloat()
            //update of text
            delay(100)
        }
    }

    LaunchedEffect(progress.floatValue) {
        val currentMinutes = AppExoPlayer.player?.currentPosition!! / 1000 / 60
        val currentSeconds = AppExoPlayer.player?.currentPosition!! / 1000 % 60

        currentPositionText.value  = String.format("%d:%02d", currentMinutes, currentSeconds)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.onBackground)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    ) {
        Row(
            modifier = Modifier
                .weight(5f)
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                .offset(y = 10.dp)
        ) {
            Button(
                onClick = {
                    when(actuallyPlayedAlbum){
                        is Album ->{
                            navController.navigate(
                            Screen.SongList(
                                listUri = listOf(actuallyPlayedAlbum.uri.toString()),
                                name = actuallyPlayedAlbum.name.toString()
                            ))
                        }
                        is List<*>->{
                            navController.navigate(
                                Screen.SongList(
                                    listUri = (actuallyPlayedAlbum as List<Album>).sortedBy { it.cdNumber }
                                        .map { it.uri.toString() }.toMutableList(),
                                    name = actuallyPlayedAlbum[0].name.toString()
                                ))
                        }
                    }

                },
                elevation = null,
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = if(image.value != null) BitmapPainter(image.value!!)
                    else painterResource(id = R.drawable.baseline_question_mark_24),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Album Cover",
                    colorFilter = if(image.value == null) {
                        if (isSystemInDarkTheme()) ColorFilter.lighting(Color.Black, Color.White)
                        else null
                    } else null,
                    modifier = Modifier
                        .width(104.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight()
            ) {
                currentSong.value?.let {
                    Text(
                        color = MaterialTheme.colorScheme.surface,
                        text = it.title.toString(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight(900),
                        maxLines = 1,
                    )
                    Text(
                        color = MaterialTheme.colorScheme.surface,
                        text = it.author.toString(),
                        maxLines = 1,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(2f)
        ){
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp)
            ) {
                Text(
                    text = currentPositionText.value,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = " / " + durationText.value,
                    color = MaterialTheme.colorScheme.surface
                )
            }
            Slider(
                value = progress.floatValue,
                onValueChange = {
                    progress.floatValue = it
                    val validTime = (it * AppExoPlayer.player?.duration!!).toInt()
                    AppExoPlayer.player?.seekTo(validTime.toLong())
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = SliderDefaults
                    .colors(
                        thumbColor = MaterialTheme.colorScheme.tertiary,
                        activeTrackColor = MaterialTheme.colorScheme.tertiary,
                        inactiveTrackColor = MaterialTheme.colorScheme.background,
                    )
            )
        }
    }
}


private fun getImageFromAlbum(
    album: Any,
    currentPlaying: Song,
): ImageBitmap? {
    val uri: String
    when(album){
        is Album -> {
            if(album.uri == currentPlaying.parentUri){
                return albumCoverCache["${album.uri}"]
            }
        }
        is List<*> ->{
            for(disc in(album as List<Album>)){
                if (disc.uri == currentPlaying.parentUri) {
                    return albumCoverCache["${disc.uri}"]
                }
            }
        }
    }
    return null
}

private fun getActuallyPlayedAlbum(
    albumList: List<Any>,
    currentPlaying: Song,
): Any? {
    for(album in albumList){
        when(album){
            is Album -> {
                if(album.uri == currentPlaying.parentUri){
                    return album
                }
            }
            is List<*> ->{
                for(disc in(album as List<Album>)){
                    if (disc.uri == currentPlaying.parentUri) {
                        return album
                    }
                }
            }
        }
    }
    return null
}