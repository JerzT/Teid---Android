package com.example.musicapp.ui.actuallyPlaying

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.image.albumCoverCache
import com.example.musicapp.logic.mediaPlayer.MediaPlayerApp
import com.example.musicapp.logic.song.Song
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun ActuallyPlayingBar(
    albumList: List<Album>,
    modifier: Modifier
) {
    val currentPlayingSong = remember { MediaPlayerApp.currentPlaying }
    val image = remember { mutableStateOf<ImageBitmap?>(null) }
    val progress = remember { mutableFloatStateOf(
        MediaPlayerApp.mediaPlayer?.currentPosition!!.toFloat() /
            MediaPlayerApp.mediaPlayer?.duration!!.toFloat()
    )}
    val durationText = remember { mutableStateOf("0:00") }
    val currentPositionText = remember { mutableStateOf("0:00") }

    LaunchedEffect(currentPlayingSong.value) {
        currentPlayingSong.value?.let {
            image.value = getImageFromAlbum(albumList, it)

            val durationMinutes = MediaPlayerApp.mediaPlayer?.duration!! / 1000 / 60
            val durationSeconds = MediaPlayerApp.mediaPlayer?.duration!! / 1000 % 60

            durationText.value = String.format("%d:%02d", durationMinutes, durationSeconds)
        }
    }

    LaunchedEffect( MediaPlayerApp.isPlaying.value) {
        while (MediaPlayerApp.isPlaying.value){
            //slider update
            progress.floatValue =
                MediaPlayerApp.mediaPlayer!!.currentPosition.toFloat() /
                        MediaPlayerApp.mediaPlayer!!.duration.toFloat()

            delay(100)
        }
    }

    LaunchedEffect(progress.floatValue) {
        val currentMinutes = MediaPlayerApp.mediaPlayer?.currentPosition!! / 1000 / 60
        val currentSeconds = MediaPlayerApp.mediaPlayer?.currentPosition!! / 1000 % 60

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
            image.value?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = "Album Cover",
                    modifier = Modifier
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
                currentPlayingSong.value?.let {
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
                    MediaPlayerApp.stopMusicButIsPlaying()
                    progress.floatValue = it
                    val validTime = (it * MediaPlayerApp.mediaPlayer?.duration!!).toInt()

                    MediaPlayerApp.mediaPlayer?.seekTo(validTime)
                },
                onValueChangeFinished = {
                    if (MediaPlayerApp.isPlaying.value){
                        MediaPlayerApp.playMusic()
                    }
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