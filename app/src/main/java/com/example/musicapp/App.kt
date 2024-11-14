package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.mainContent.AlbumsList
import com.example.musicapp.mainContent.DirectorySelectionUi
import com.example.musicapp.mainContent.SongsList
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

    val settings = SettingsDataStore(context)

    val albumsList = remember { mutableStateListOf<Album>() }
    //albumList loading and synchronizing
    LaunchedEffect(Unit) {
        settings.directoryPathFlow.collect { directoryPath ->
            uri.value = changeNotValidDirectoryPathToUri(directoryPath)
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
    }

    val navController = rememberNavController()


    MusicAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavHost(
                navController = navController,
                startDestination = if (uri.value == null) Screen.GetUri.route else Screen.AlbumList.route
            ) {
                composable(
                    route = Screen.GetUri.route
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBarCustom(
                                title = "Get Directory"
                            )
                        },
                        bottomBar = { if (false) BottomBarCustom() },
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            DirectorySelectionUi(
                                uri = uri,
                                albumsList = albumsList,
                                navController = navController
                            )
                        }
                    }
                }
                composable(
                    route = Screen.AlbumList.route
                ) {
                    val searchText = remember { mutableStateOf("")}
                    Scaffold(
                        topBar = {
                            TopAppBarCustom(
                                title = "Library"
                            )
                        },
                        bottomBar = { if (true) BottomBarCustom() },
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .zIndex(3f),
                                searchText = searchText,
                            )
                            AlbumsList(
                                albumsList = albumsList,
                                navController = navController,
                                searchText = searchText,
                            )
                        }
                    }
                }
                composable(
                    route = Screen.SongList.route + "/{uri}/{name}",
                    arguments = listOf(
                        navArgument("uri") {
                            type = NavType.StringType
                            defaultValue = "a"
                            nullable = true
                        }
                    )
                ) { entry ->
                    val searchText = remember { mutableStateOf("")}
                    Scaffold(
                        topBar = {
                            TopAppBarCustom(
                                title = entry.arguments?.getString("name").toString(),
                                navController = navController
                            )
                        },
                        bottomBar = { if (true) BottomBarCustom() },
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .zIndex(3f),
                                searchText = searchText,
                            )
                            SongsList(
                                uri = entry.arguments?.getString("uri")?.toUri(),
                                searchText = searchText,
                            )
                        }
                    }
                }
            }
        }
    }
}

open class Screen(
    val route: String
){
    object AlbumList: Screen("AlbumList")
    object SongList: Screen("SongList")
    object GetUri: Screen("GetUri")

    fun withArgs(vararg args: String): String{
        var validRoute = ""

        validRoute += route
        args.forEach { arg ->
            validRoute += ("/$arg")
        }

        return validRoute
    }
}