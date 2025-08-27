/*
package com.example.musicapp

import android.net.Uri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.musicapp.newLogic.album.Album
import com.example.musicapp.newLogic.album.connectDiscFromAlbums
import com.example.musicapp.logic.database.getAlbumsFromDatabase
import com.example.musicapp.logic.album.getAlbumsFromDirectory
import com.example.musicapp.newLogic.album.synchronizeAlbums
import com.example.musicapp.newLogic.directory.changeNotValidDirectoryPathToUri
import com.example.musicapp.logic.image.cacheAlbumCovers
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.ui.bottomBar.BottomBarCustom
import com.example.musicapp.ui.directorySelection.DirectorySelectionUi
import com.example.musicapp.ui.lists.albumsList.AlbumsList
import com.example.musicapp.ui.lists.songsList.SongsList
import com.example.musicapp.ui.searchBar.SearchBar
import com.example.musicapp.ui.topAppBar.TopAppBarCustom
import kotlinx.serialization.Serializable

fun App(uri: Uri?) {
    val context = LocalContext.current

    val uri = remember { mutableStateOf<Uri?>(uri) }


    val albumsList = remember { mutableStateListOf<Any>() }
    //albumList loading and synchronizing
    LaunchedEffect(Unit) {
        settings.directoryPathFlow.collect { directoryPath ->
            uri.value = changeNotValidDirectoryPathToUri(directoryPath)
            if (uri.value != null) {

                val albumsFromDatabase = getAlbumsFromDatabase(context)
                    .apply { sortBy { if (it is Album) it.name else ""  } }
                val connectedAlbumsFromDatabase = connectDiscFromAlbums(albumsFromDatabase)

                albumsList.addAll(connectedAlbumsFromDatabase)
                cacheAlbumCovers(connectedAlbumsFromDatabase, context)

                val albumsInDirectory = getAlbumsFromDirectory(
                    context = context,
                    uri = uri.value
                ).apply { sortBy { if (it is Album) it.name else ""  } }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = if (uri.value == null) Screen.GetUri else Screen.AlbumList
        ) {
            //Get Directory
            composable<Screen.GetUri>{
                Scaffold(
                    topBar = {
                        TopAppBarCustom(
                            title = "Get Directory",
                            currentScreen = GET_URI
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
            composable<Screen.AlbumList>{
                Scaffold(
                    topBar = {
                        TopAppBarCustom(
                            title = "Library",
                            navController = navController,
                            currentScreen = ALBUM_LIST
                        )
                    },
                    bottomBar = { if (AppExoPlayer.haveSongs.value)
                        BottomBarCustom(
                            albumList = albumsList,
                            navController = navController,
                        )},
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
            composable<Screen.SongList>{
                val searchText = remember { mutableStateOf("")}
                val args = it.toRoute<Screen.SongList>()

                Scaffold(
                    topBar = {
                        TopAppBarCustom(
                            title = args.name.toString(),
                            navController = navController,
                            currentScreen = SONG_LIST
                        )
                    },
                    bottomBar = { if(AppExoPlayer.haveSongs.value)
                        BottomBarCustom(
                            albumList = albumsList,
                            navController = navController,
                            songListUri = args.listUri
                        )},
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
                            listUri = args.listUri,
                            searchText = searchText,
                            albumsList = albumsList,
                        )
                    }
                }
            }
            //Settings
            composable<Screen.Settings> {
                Scaffold(
                    topBar = {
                        TopAppBarCustom(
                            title = "Settings",
                            navController = navController,
                            currentScreen = SETTINGS
                        )
                    },
                    bottomBar = { if (AppExoPlayer.haveSongs.value)
                        BottomBarCustom(
                            albumList = albumsList,
                            navController = navController,
                        )},
                ){}
            }
        }
    }
}

open class Screen
{
    @Serializable
    object AlbumList
    @Serializable
    data class SongList(
        val listUri: List<String>,
        val name: String?,
    )
    @Serializable
    object GetUri
    @Serializable
    object Settings
}

const val ALBUM_LIST = "AlbumList"
const val SONG_LIST = "SongList"
const val GET_URI = "GetUri"
const val SETTINGS = "Settings"
*/
