package com.example.musicapp.mainContent

import androidx.compose.runtime.Composable
import com.example.musicapp.musicFilesUsage.Album

@Composable
fun MainContent(
    albumsList: List<Album>
) {


    AlbumsList(albumsList = albumsList)


}