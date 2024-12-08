package com.example.musicapp.logic.album

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.musicapp.logic.database.setUpDatabase

@SuppressLint("Range")
fun getAlbumsFromDatabase(
    context: Context,
): MutableList<Any> {
    val database = setUpDatabase(context)
    //get saved albums
    val albumsList: MutableList<Any> = mutableListOf()
    val getAlbumResult = database.getAlbums()
    getAlbumResult.use { albumRow ->
        while (albumRow.moveToNext()){
            val name: String? = if(albumRow.getString(albumRow.getColumnIndex("name")) != "")
                albumRow.getString(albumRow.getColumnIndex("name")) else null

            val uri: Uri = albumRow.getString(albumRow.getColumnIndex("uri")).toUri()

            val cover: Uri? = if(albumRow.getString(albumRow.getColumnIndex("cover")) != "")
                albumRow.getString(albumRow.getColumnIndex("cover")).toUri() else null

            val artist: String? = if(albumRow.getString(albumRow.getColumnIndex("artist")) != "")
                albumRow.getString(albumRow.getColumnIndex("artist")) else null

            val year: String? = if(albumRow.getString(albumRow.getColumnIndex("year")) != "")
                albumRow.getString(albumRow.getColumnIndex("year")) else null

            val cdNumber: Int = albumRow.getInt(albumRow.getColumnIndex("cd_number"))

            val album = Album(
                name = name,
                uri = uri,
                cover = cover,
                artist = artist,
                year = year,
                cdNumber = cdNumber,
            )

            albumsList += album
        }
    }
    getAlbumResult.close()
    return albumsList
}