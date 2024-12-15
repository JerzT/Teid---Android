package com.example.musicapp.ui.lists.songsList

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musicapp.logic.song.Song

@SuppressLint("DefaultLocale")
@Composable
fun DiscInfoHeader(
    songsList: List<Song>,
    discIndex: Int,
){
    val songsCount = songsList.count()
    var albumLength: Int = 0
    for(song in songsList){
        albumLength += song.length
    }
    val minutes = (albumLength/ 1000) / 60
    val seconds = (albumLength / 1000) % 60

    val albumLengthString = String.format("%d:%02d", minutes, seconds)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier =  Modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp)
        ) {
            Text(
                text = "Disk ${discIndex+1}",
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = "Time: $albumLengthString",
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Songs: $songsCount",
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
        }
    }
}