package com.example.musicapp.mainContent

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists

@Composable
fun AlbumsList() {
    val albumsList by AlbumsWhichExists.list.collectAsState()

    for (album in albumsList) {
        Button(onClick = {
        }) {
            Text(text = album.name.toString())
        }
    }
}