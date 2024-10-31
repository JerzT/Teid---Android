package com.example.musicapp.mainContent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.musicFilesUsage.Song

@Composable
fun SongsList(
    songsList: List<Song>,
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (song in songsList){
            Button(onClick = {
                testPlaying(
                    context = context,
                    uri = song.uri,
                )
            }) {
                Text(
                    text = song.title ?: ""
                )
            }
        }
    }
}