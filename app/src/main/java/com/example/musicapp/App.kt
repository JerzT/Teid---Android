package com.example.musicapp

import android.annotation.SuppressLint
import android.os.Build
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
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists
import com.example.musicapp.musicFilesUsage.GetDirectory
import com.example.musicapp.musicFilesUsage.setUpDatabase
import com.example.musicapp.onStartApp.getAlbumsFromDirectory
import com.example.musicapp.onStartApp.synchronizeAlbums
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme
import getAlbumsFromDatabase
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

    val context = LocalContext.current

    val database = setUpDatabase(context)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val albumsFromDatabase = getAlbumsFromDatabase(database = database)
            val albumsInDirectory = getAlbumsFromDirectory(context = context)
            AlbumsWhichExists.list = albumsInDirectory
            synchronizeAlbums(
                albumsFromDatabase = albumsFromDatabase,
                albumsInDirectory = albumsInDirectory,
                database = database,
            )
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
                    database = database,
                )
            }
        }
    }
}