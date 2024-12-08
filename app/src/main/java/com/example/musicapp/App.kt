package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
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
import com.example.musicapp.logic.album.connectDiscFromAlbums
import com.example.musicapp.logic.album.getAlbumsFromDatabase
import com.example.musicapp.logic.album.getAlbumsFromDirectory
import com.example.musicapp.logic.album.synchronizeAlbums
import com.example.musicapp.logic.directory.changeNotValidDirectoryPathToUri
import com.example.musicapp.logic.image.cacheAlbumCovers
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.ui.bottomBar.BottomBarCustom
import com.example.musicapp.ui.directorySelection.DirectorySelectionUi
import com.example.musicapp.ui.lists.AlbumsList
import com.example.musicapp.ui.lists.SongsList
import com.example.musicapp.ui.searchBar.SearchBar
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.ui.topAppBar.TopAppBarCustom

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition", "Range")
@Composable
fun App() {
    val context = LocalContext.current

    val uri = remember { mutableStateOf<Uri?>(null) }

    val settings = SettingsDataStore(context)

    val albumsList = remember { mutableStateListOf<Any>() }
    //albumList loading and synchronizing
    LaunchedEffect(Unit) {
        settings.directoryPathFlow.collect { directoryPath ->
            uri.value = changeNotValidDirectoryPathToUri(directoryPath)
            if (uri.value != null) {

                val albumsFromDatabase = getAlbumsFromDatabase(context)/*.apply { sortBy { it.name } }*/
                val connectedAlbumsFromDatabase = connectDiscFromAlbums(albumsFromDatabase)

                albumsList.addAll(connectedAlbumsFromDatabase)
                cacheAlbumCovers(connectedAlbumsFromDatabase, context)

                val albumsInDirectory = getAlbumsFromDirectory(
                    context = context,
                    uri = uri.value
                )/*.apply { sortBy { it.name } }*/
                val connectedAlbumsFromDirectory = connectDiscFromAlbums(albumsInDirectory)

                albumsList.clear()
                albumsList.addAll(connectedAlbumsFromDirectory)
                cacheAlbumCovers(connectedAlbumsFromDirectory, context)

                synchronizeAlbums(
                    albumsFromDatabase = albumsFromDatabase,
                    albumsInDirectory = albumsInDirectory,
                    context = context,
                )
            }
        }
    }

    val navController = rememberNavController()
    val albumListState = remember { LazyListState() }
    val albumSearchText = remember { mutableStateOf("")}


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
                //Get Directory
                composable(
                    route = Screen.GetUri.route
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBarCustom(
                                title = "Get Directory"
                            )
                        },
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
                //Album List
                composable(
                    route = Screen.AlbumList.route
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBarCustom(
                                title = "Library"
                            )
                        },
                        bottomBar = { if (AppExoPlayer.haveSongs.value)
                            BottomBarCustom(albumList = albumsList) },
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .zIndex(3f),
                                searchText = albumSearchText,
                            )
                            AlbumsList(
                                albumsList = albumsList,
                                navController = navController,
                                searchText = albumSearchText,
                                state = albumListState,
                            )
                        }
                    }
                }
                //Song List
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
                        bottomBar = { if(AppExoPlayer.haveSongs.value)
                            BottomBarCustom(albumList = albumsList)},
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