package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.mainContent.AlbumsList
import com.example.musicapp.mainContent.DirectorySelectionUi
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.onStartApp.changeNotValidDirectoryPathToUri
import com.example.musicapp.onStartApp.getAlbumsFromDirectory
import com.example.musicapp.onStartApp.synchronizeAlbums
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.settings.SettingsDataStore
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme
import getAlbumsFromDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition", "Range")
@Composable
fun App() {
    val context = LocalContext.current

    val uri = remember { mutableStateOf<Uri?>(null) }

    val albumsList = remember { mutableStateListOf<Album>() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val settings = SettingsDataStore(context)

        settings.directoryPathFlow.collect { directoryPath ->
            uri.value = changeNotValidDirectoryPathToUri(directoryPath)

            if (uri.value != null) {
                isLoading.value = true

                // Async data loading
                val albumsFromDatabase = getAlbumsFromDatabase(context)

                albumsList.addAll(albumsFromDatabase)

                isLoading.value = false

                val albumsInDirectory = getAlbumsFromDirectory(context = context, uri = uri.value)

                albumsList.clear()
                albumsList.addAll(albumsInDirectory)

                synchronizeAlbums(
                    albumsFromDatabase = albumsFromDatabase,
                    albumsInDirectory = albumsInDirectory,
                    context = context,
                )
            }
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
                        .zIndex(1f)
                )
                if (uri.value == null) {
                    DirectorySelectionUi(
                        uri = uri,
                        albumsList = albumsList
                    )
                } else {
                    if (isLoading.value) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator() // Loading spinner
                        }
                    } else {
                        AlbumsList(albumsList = albumsList)
                    }
                }
            }
        }
    }
}