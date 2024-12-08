package com.example.musicapp.ui.directorySelection

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.directory.GetDirectory

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DirectorySelectionUi(
    uri: MutableState<Uri?>,
    albumsList: MutableList<Any>,
    navController: NavController,
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        GetDirectory(
            uri = uri,
            albumsList = albumsList,
            navController = navController,
        )
    }
}