package com.example.musicapp.mainContent

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicapp.musicFilesUsage.MediaPlayerApp
import com.example.musicapp.musicFilesUsage.Song

@Composable
fun SongsList(
    songsList: List<Song>,
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surface,
            thickness = 1.dp
        )
        for (song in songsList){
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                onClick = {
                    testPlaying(
                        context = context,
                        uri = song.uri,
                    )
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
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = song.title ?: "",
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(2f)

                    )
                    Text(
                        text = song.length.toString(),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

private fun testPlaying(
    context: Context,
    uri: Uri
){
    MediaPlayerApp.addMusicToPlay(
        context = context,
        uri = uri
    )
    MediaPlayerApp.playMusic()
}