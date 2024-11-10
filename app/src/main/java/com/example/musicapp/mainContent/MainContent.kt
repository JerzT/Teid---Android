package com.example.musicapp.mainContent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.Song

@RequiresApi(Build.VERSION_CODES.P)
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
        songsList.sortBy { song -> song.number }
        SongsList(
            songsList = songsList

        )
    }
}

