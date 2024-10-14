package com.example.musicapp.onStartApp

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.findAlbums
import com.example.musicapp.settings.SettingsDataStore

@RequiresApi(Build.VERSION_CODES.P)
suspend fun getAlbumsFromDirectory(
    context: Context,
): MutableList<Album> {
    var uri: Uri?
    val settings = SettingsDataStore(context)

    val albumsInDirectory: MutableList<Album> = mutableListOf()
    settings.directoryPathFlow.collect { directoryPath ->
        uri = directoryPath?.toUri()
        findAlbums(
            uri = uri,
            context = context,
            albumsList = albumsInDirectory
        ).await()
    }
    return albumsInDirectory
}