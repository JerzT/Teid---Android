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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.mainContent.AlbumsList
import com.example.musicapp.mainContent.DirectorySelectionUi
import com.example.musicapp.mainContent.MainContent
import com.example.musicapp.mainContent.cacheAlbumCovers
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.onStartApp.changeNotValidDirectoryPathToUri
import com.example.musicapp.onStartApp.getAlbumsFromDirectory
import com.example.musicapp.onStartApp.synchronizeAlbums
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.settings.SettingsDataStore
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme
import getAlbumsFromDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition", "Range")
@Composable
fun App() {
    val context = LocalContext.current

    val uri = remember { mutableStateOf<Uri?>(null) }

    val albumsList = remember { mutableStateListOf<Album>() }

    GlobalScope.launch {
        val settings = SettingsDataStore(context)

        settings.directoryPathFlow.collect { directoryPath ->
            uri.value = changeNotValidDirectoryPathToUri(directoryPath)
        }

        settings.directoryPathFlow.collect{ albumsCovers ->
            Log.v("test1", albumsCovers.toString())
        }
    }

    LaunchedEffect(Unit) {
        if (uri.value != null) {

            val albumsFromDatabase = getAlbumsFromDatabase(context).apply { sortBy { it.name } }

            albumsList.addAll(albumsFromDatabase)
            cacheAlbumCovers(albumsFromDatabase, context)

            val albumsInDirectory = getAlbumsFromDirectory(
                context = context,
                uri = uri.value
            ).apply { sortBy { it.name } }

            albumsList.clear()
            albumsList.addAll(albumsInDirectory)
            cacheAlbumCovers(albumsInDirectory, context)

            synchronizeAlbums(
                albumsFromDatabase = albumsFromDatabase,
                albumsInDirectory = albumsInDirectory,
                context = context,
            )
        }
    }

    MusicAppTheme {
        Scaffold(
            topBar = { TopAppBarCustom() },
            bottomBar = { if (true) BottomBarCustom() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SearchBar(
                    modifier = Modifier
                        .zIndex(2f)
                )
                if (uri.value == null) {
                    DirectorySelectionUi(
                        uri = uri,
                        albumsList = albumsList
                    )
                } else {
                    MainContent(
                        albumsList = albumsList,
                    )
                }
            }
        }
    }
}