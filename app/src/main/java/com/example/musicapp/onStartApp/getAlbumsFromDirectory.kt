package com.example.musicapp.onStartApp

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.findAlbums
import com.example.musicapp.settings.SettingsDataStore

@RequiresApi(Build.VERSION_CODES.P)
suspend fun getAlbumsFromDirectory(
    context: Context,
    uri: Uri?
): MutableList<Album> {

    val albumsInDirectory: MutableList<Album> = mutableListOf()

    findAlbums(
        uri = uri,
        context = context,
        albumsList = albumsInDirectory
    ).await()

    return albumsInDirectory
}