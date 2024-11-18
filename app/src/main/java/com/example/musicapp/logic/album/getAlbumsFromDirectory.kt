package com.example.musicapp.logic.album

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

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