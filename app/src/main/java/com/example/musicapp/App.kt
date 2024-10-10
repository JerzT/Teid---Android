package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists
import com.example.musicapp.musicFilesUsage.GetDirectory
import com.example.musicapp.musicFilesUsage.setUpDatabase
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.settings.SettingsDataStore
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "Range"
)
@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 650,
)
@Composable
fun App(){
    val isPlaying = true
    var uri: Uri?

    val context = LocalContext.current

    val settings = SettingsDataStore(context)

    val database = setUpDatabase(context)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            launch {
                //get saved albums
                val albums = database.getAlbums()
                if(albums != null){
                    albums.use { albumRow ->
                        while (albumRow.moveToNext()){
                            val name: String? = albumRow.getString(albumRow.getColumnIndex("name"))
                            val uri: Uri = albumRow.getString(albumRow.getColumnIndex("uri")).toUri()
                            val cover: Uri = albumRow.getString(albumRow.getColumnIndex("cover")).toUri()
                            val artist: String? = albumRow.getString(albumRow.getColumnIndex("artist"))
                            val year: String? = albumRow.getString(albumRow.getColumnIndex("year"))
                            val cdNumber: Int = albumRow.getInt(albumRow.getColumnIndex("cd_number"))

                            val album = Album(
                                name = name,
                                uri = uri,
                                cover = cover,
                                artist = artist,
                                year = year,
                                cdNumber = cdNumber,
                            )
                            //Log.v("test1", album.toString())
                        }
                    }
                    albums.close()
                }


                settings.directoryPathFlow.collect { directoryPath ->
                    uri = directoryPath?.toUri()
                }
            }
            launch {

            }
        }
    }

    MusicAppTheme {
        Scaffold(
            topBar = { TopAppBarCustom() },
            bottomBar = { if(isPlaying) BottomBarCustom() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SearchBar(
                    modifier = Modifier
                        .zIndex(1f)
                )
                GetDirectory(
                    database = database
                )
            }
        }
    }
}