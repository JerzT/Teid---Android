package com.example.musicapp.onStartApp

import android.content.Context
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists
import com.example.musicapp.musicFilesUsage.DBHelper
import com.example.musicapp.musicFilesUsage.setUpDatabase

fun synchronizeAlbums(
    albumsFromDatabase: MutableList<Album>,
    albumsInDirectory: MutableList<Album>,
    context: Context,
){
    val database = setUpDatabase(context)
    for (i in albumsFromDatabase.size - 1 downTo 0) {
        for (j in albumsInDirectory.size - 1 downTo 0) {
            if (albumsFromDatabase[i] == albumsInDirectory[j]) {

                albumsFromDatabase.removeAt(i)
                albumsInDirectory.removeAt(j)

                break
            }
        }
    }
    for (album in albumsFromDatabase){
        database.deleteAlbum(album)
    }
    for(album in albumsInDirectory){
        database.addAlbum(album)
    }
    AlbumsWhichExists.list = albumsInDirectory
}