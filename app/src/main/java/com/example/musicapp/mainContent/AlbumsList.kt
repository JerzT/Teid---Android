package com.example.musicapp.mainContent

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.musicapp.musicFilesUsage.Album

@Composable
fun AlbumsList(
    albumsList: List<Album>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        for (album in albumsList) {
            Button(
                shape = RoundedCornerShape(10f),
                onClick = {
            }) {
                AsyncImage(
                    model = album.cover,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,)
                Text(
                    text = album.name.toString(),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumsListPreview() {
    // Mock data for albums
    val sampleAlbums = listOf(
        Album(name = "Album 1", uri = "test".toUri(), artist = "test", cover = Uri.parse("file:///path_to_image_1"), year = "0", cdNumber = 1),
        Album(name = "Album 2", uri = "test".toUri(), artist = "test", cover = Uri.parse("file:///path_to_image_2"), year = "0", cdNumber = 1),
        Album(name = "Album 3", uri = "test".toUri(), artist = "test", cover = Uri.parse("file:///path_to_image_3"), year = "0", cdNumber = 1)
    )
    AlbumsList(albumsList = sampleAlbums)
}