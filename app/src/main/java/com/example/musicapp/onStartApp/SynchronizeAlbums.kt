package com.example.musicapp.onStartApp

import android.util.Log
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists
import com.example.musicapp.musicFilesUsage.DBHelper

suspend fun SynchronizeAlbums(
    albumsFromDatabase: MutableList<Album>,
    albumsInDirectory: MutableList<Album>,
    database: DBHelper,
){
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
        Log.v("test1", "deleted")
        database.deleteAlbum(album)
    }
    for(album in albumsInDirectory){
        AlbumsWhichExists.list += album
        database.addAlbum(album)
    }
}