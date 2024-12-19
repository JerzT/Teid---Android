package com.example.musicapp.ui.lists.songsList

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.song.Song

@SuppressLint("DefaultLocale", "CoroutineCreationDuringComposition")
@Composable
fun SongItem(
    song: Song,
    songsList: List<Song>,
    albumsList: List<Any>
){
    val actuallyPlayingSong = remember { AppExoPlayer.currentSong }
    val isPlaying = remember { mutableStateOf(false) }

    LaunchedEffect(actuallyPlayingSong.value) {
        isPlaying.value = song == AppExoPlayer.currentSong.value
    }

    val minutes = (song.length / 1000) / 60
    val seconds = (song.length / 1000) % 60

    val songDurationFormated = String.format("%d:%02d", minutes, seconds)

    Button(
        contentPadding = PaddingValues(5.dp, 0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor =
            if(isPlaying.value) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.background,
        ),
        onClick = {
            AppExoPlayer.setPlaylist(
                songPlaylist = songsList,
                albumsList = albumsList,
            )
            AppExoPlayer.playMusic()
            AppExoPlayer.setPlaylistToSelectedSong(song, songsList)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = song.number.toString(),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = song.title ?: "",
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(9f)

            )
            Text(
                text = song.timePlayed.toString(),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = songDurationFormated,
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(3f)
            )
        }
    }
}