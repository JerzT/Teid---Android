package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.mainContent.AlbumsList
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists
import com.example.musicapp.musicFilesUsage.GetDirectory
import com.example.musicapp.musicFilesUsage.findAlbums
import com.example.musicapp.musicFilesUsage.setUpDatabase
import com.example.musicapp.onStartApp.getAlbumsFromDirectory
import com.example.musicapp.onStartApp.synchronizeAlbums
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.settings.SettingsDataStore
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme
import getAlbumsFromDatabase
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "Range"
)
@Composable
fun App(){
    val context = LocalContext.current

    val database = setUpDatabase(context)

    val coroutineScope = rememberCoroutineScope()

    val isUriExists = remember{ mutableStateOf(false)}

    var uri: Uri? = null

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val settings = SettingsDataStore(context)

            settings.directoryPathFlow.collect { directoryPath ->
                uri = directoryPath?.toUri()
                if(uri!=null){
                    isUriExists.value = true
                    val albumsFromDatabase = getAlbumsFromDatabase(database = database)
                    AlbumsWhichExists.list = albumsFromDatabase
                    val albumsInDirectory = getAlbumsFromDirectory(context = context)
                    AlbumsWhichExists.list = albumsInDirectory
                    synchronizeAlbums(
                        albumsFromDatabase = albumsFromDatabase,
                        albumsInDirectory = albumsInDirectory,
                        database = database,
                    )
                }
            }
        }
    }

    MusicAppTheme {
        Scaffold(
            topBar = { TopAppBarCustom() },
            bottomBar = { if(true) BottomBarCustom() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SearchBar(
                    modifier = Modifier
                        .zIndex(1f)
                )
                if(!isUriExists.value){
                    Log.v("test1", uri.toString())
                    GetDirectory(
                        database = database,
                        isUriExists = isUriExists,
                    )
                }
                else{
                    AlbumsList()
                }
            }
        }
    }
}