package com.example.musicapp.mainContent

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.MediaPlayerApp
import com.example.musicapp.musicFilesUsage.Song

@Composable
fun MainContent(
    albumsList: List<Album>
) {
    val songsList = remember{ mutableStateListOf<Song>()}

    if (songsList.isEmpty()){
        AlbumsList(
            albumsList = albumsList,
            songsList = songsList,
        )
    }
    else{
        SongsList(
            songsList = songsList
        )
    }
}

fun testPlaying(
    context: Context,
    uri: Uri
){
    MediaPlayerApp.addMusicToPlay(
        context = context,
        uri = uri
    )
    MediaPlayerApp.playMusic()
}