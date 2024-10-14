package com.example.musicapp.onStartApp

import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.DBHelper

fun synchronizeAlbums(
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
        database.deleteAlbum(album)
    }
    for(album in albumsInDirectory){
        database.addAlbum(album)
    }
}