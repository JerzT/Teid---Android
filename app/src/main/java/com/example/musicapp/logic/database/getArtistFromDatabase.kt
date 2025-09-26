package com.example.musicapp.logic.database

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.musicapp.logic.artist.Artist

@SuppressLint("Range")
fun getArtistsFromDatabase(
    context: Context,
): MutableSet<Artist> {
    val database = setUpDatabase(context)
    //get saved albums
    val artistSet = mutableSetOf<Artist>()
    val getAlbumResult = database.getArtists()
    getAlbumResult.use { albumRow ->
        while (albumRow.moveToNext()){
            val name: String? = if(albumRow.getString(albumRow.getColumnIndex("name")) != "")
                albumRow.getString(albumRow.getColumnIndex("name")) else null

            val uri: Uri = albumRow.getString(albumRow.getColumnIndex("uri")).toUri()

            val cover: Uri? = if(albumRow.getString(albumRow.getColumnIndex("cover")) != "")
                albumRow.getString(albumRow.getColumnIndex("cover")).toUri() else null

            val artist = Artist(
                name,
                cover,
                uri,
            )

            artistSet.add(artist)
        }
    }
    getAlbumResult.close()
    return artistSet
}