package com.example.musicapp.logic.album

import android.content.Context
import com.example.musicapp.logic.database.setUpDatabase

fun synchronizeAlbums(
    albumsFromDatabase: MutableList<Album>,
    albumsInDirectory: MutableList<Album>,
    context: Context,
){
    val database = setUpDatabase(context)


    val albumsFromDatabaseSet = albumsFromDatabase.toSet()
    val albumsInDirectorySet = albumsInDirectory.toSet()

    val albumsToDelete = albumsFromDatabaseSet - albumsInDirectorySet
    val albumsToAdd = albumsInDirectorySet - albumsFromDatabaseSet

    for (album in albumsToDelete){
        database.deleteAlbum(album)
    }
    for(album in albumsToAdd){
        database.addAlbum(album)
    }
}