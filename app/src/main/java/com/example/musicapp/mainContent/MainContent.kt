package com.example.musicapp.mainContent

import android.os.Build
import android.util.Log
import androidx.annotation.NavigationRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavArgs
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.Song

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainContent(
    albumsList: List<Album>
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ContentScreen.AlbumList.route
    ){
        composable(route = ContentScreen.AlbumList.route) {
            AlbumsList(
                albumsList = albumsList,
                navController = navController
            )
        }
        composable(
            route = ContentScreen.SongList.route + "/{uri}",
            arguments = listOf(
                navArgument("uri"){
                    type = NavType.StringType
                    defaultValue = "a"
                    nullable = true
                }
            )
        ) { entry ->
            SongsList(
                uri = entry.arguments?.getString("uri")?.toUri()
            )
        }
    }
}

open class ContentScreen(
    val route: String
){
    object AlbumList: ContentScreen("AlbumList")
    object SongList: ContentScreen("SongList")

    fun withArgs(vararg args: String): String{
        var validRoute = ""

        validRoute += route
        args.forEach { arg ->
           validRoute += ("/$arg")
        }

        return validRoute
    }
}

