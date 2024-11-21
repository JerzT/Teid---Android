package com.example.musicapp.ui.actuallyPlaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActuallyPlayingBar(
    albumList: List<Album>,
    modifier: Modifier
) {
    val currentPlayingSong = remember { MediaPlayerApp.currentPlaying }
    val image = remember { mutableStateOf<ImageBitmap?>(null) }
    val progress = remember { mutableFloatStateOf(0f) }

    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(currentPlayingSong.value) {
        currentPlayingSong.value?.let {
            image.value = getImageFromAlbum(albumList, it)

            while (MediaPlayerApp.mediaPlayer!!.isPlaying){
                progress.floatValue = MediaPlayerApp.mediaPlayer!!.currentPosition.toFloat() / MediaPlayerApp.mediaPlayer!!.duration.toFloat()
                delay(100)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    ) {
        Row {
            image.value?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = "Album Cover",
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .height(80.dp)
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
                    .height(96.dp)
                    .padding(vertical = 8.dp)
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
        ){
/*            Text(
                text = "A",
                modifier = Modifier
                    .zIndex(1f)
            )*/

            Slider(
                value = progress.floatValue,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                    inactiveTrackColor = MaterialTheme.colorScheme.background,
                 ),
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